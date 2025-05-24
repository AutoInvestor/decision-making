package io.autoinvestor.infrastructure.listeners;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.core.ApiService.Listener;
import com.google.api.core.ApiService.State;
import com.google.cloud.pubsub.v1.AckReplyConsumer;
import com.google.cloud.pubsub.v1.MessageReceiver;
import com.google.cloud.pubsub.v1.Subscriber;
import com.google.pubsub.v1.ProjectSubscriptionName;
import com.google.pubsub.v1.PubsubMessage;
import io.autoinvestor.application.RegisterDecisionCommand;
import io.autoinvestor.application.RegisterDecisionCommandHandler;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
@Profile("prod")
public class PubsubNewsEventSubscriber {

    private final RegisterDecisionCommandHandler commandHandler;
    private final PubsubEventMapper eventMapper;
    private final ProjectSubscriptionName subscriptionName;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private Subscriber subscriber;

    public PubsubNewsEventSubscriber(
            RegisterDecisionCommandHandler commandHandler,
            PubsubEventMapper eventMapper,
            @Value("${GCP_PROJECT}") String projectId,
            @Value("${PUBSUB_SUBSCRIPTION_MARKET_FEELING}") String subscriptionId) {
        this.commandHandler   = commandHandler;
        this.eventMapper      = eventMapper;
        this.subscriptionName = ProjectSubscriptionName.of(projectId, subscriptionId);
    }

    @PostConstruct
    public void listen() {
        MessageReceiver receiver = this::processMessage;

        this.subscriber = Subscriber.newBuilder(subscriptionName, receiver).build();
        this.subscriber.addListener(new Listener() {
            @Override public void failed(State from, Throwable failure) {}
        }, Runnable::run);
        subscriber.startAsync().awaitRunning();
    }

    @PreDestroy
    public void stop() {
        if (subscriber != null) {
            subscriber.stopAsync();
        }
    }

    private void processMessage(PubsubMessage message, AckReplyConsumer consumer) {
        try {
            Map<String, Object> raw = this.objectMapper.readValue(
                    message.getData().toByteArray(), new TypeReference<>() {});

            PubsubEvent event = this.eventMapper.fromMap(raw);

            if ("ASSET_FEELING_DETECTED".equals(event.getType())) {
                RegisterDecisionCommand cmd = new RegisterDecisionCommand(
                        (String) event.getPayload().get("assetId"),
                        (int) event.getPayload().get("feeling")
                );
                this.commandHandler.handle(cmd);
            }

            consumer.ack();
        } catch (Exception ex) {
            consumer.nack();
        }
    }
}
