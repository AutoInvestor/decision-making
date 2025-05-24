package io.autoinvestor.infrastructure.repositories;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
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
        JsonNode payloadNode = json.valueToTree(evt.getPayload());
        return new EventDocument(
                evt.getId().toString(),
                evt.getAggregateId().toString(),
                evt.getType(),
                payloadNode,          //  JsonNode, not String
                evt.getOccurredAt(),
                evt.getVersion()
        );
    }

    public Event<?> toDomain(EventDocument doc) throws JsonProcessingException {
        EventId id         = EventId.of(doc.getId());
        DecisionId aggId   = DecisionId.from(doc.getAggregateId());
        Date occurredAt    = doc.getOccurredAt();
        int version        = doc.getVersion();

        switch (doc.getType()) {
            case DecisionTakenEvent.TYPE -> {
                DecisionTakenEventPayload payload =
                        json.treeToValue(doc.getPayload(), DecisionTakenEventPayload.class);

                return DecisionTakenEvent.hydrate(id, aggId, payload, occurredAt, version);
            }

            default -> throw new IllegalArgumentException(
                    "Unknown event type: " + doc.getType());
        }
    }
}
