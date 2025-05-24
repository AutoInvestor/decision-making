package io.autoinvestor.infrastructure.repositories;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.autoinvestor.domain.events.*;
import io.autoinvestor.domain.model.DecisionId;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;

@Component
public class EventMapper {

    private final ObjectMapper json = new ObjectMapper();

    public <P extends EventPayload> EventDocument toDocument(Event<P> evt) {
        try {
            String payloadJson = json.writeValueAsString(evt.getPayload());
            return new EventDocument(
                    evt.getId().toString(),
                    evt.getAggregateId().toString(),
                    evt.getType(),
                    payloadJson,
                    evt.getOccurredAt(),
                    evt.getVersion()
            );
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize event payload", e);
        }
    }

    public Event<?> toDomain(EventDocument doc) {
        try {
            EventId id = EventId.of(doc.getId());
            DecisionId aggregateId = DecisionId.from(doc.getAggregateId());
            Date occurredAt  = doc.getOccurredAt();
            int version = doc.getVersion();

            switch (doc.getType()) {
                case DecisionTakenEvent.TYPE:
                    DecisionTakenEventPayload payload =
                            json.readValue(
                                    doc.getPayload(),
                                    DecisionTakenEventPayload.class
                            );

                    return DecisionTakenEvent.hydrate(
                            id,
                            aggregateId,
                            payload,
                            occurredAt,
                            version
                    );
                default:
                    throw new IllegalArgumentException(
                            "Unknown event type: " + doc.getType()
                    );
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse event payload", e);
        }
    }
}
