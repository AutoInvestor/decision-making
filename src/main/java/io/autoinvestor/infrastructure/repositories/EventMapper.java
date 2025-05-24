package io.autoinvestor.infrastructure.repositories;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.autoinvestor.domain.events.*;
import io.autoinvestor.domain.model.DecisionId;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

@Component
public class EventMapper {

    private final ObjectMapper json = new ObjectMapper();

    public <P extends EventPayload> EventDocument toDocument(Event<P> evt) {
        Map<String, Object> payloadMap =
                json.convertValue(evt.getPayload(), new TypeReference<Map<String, Object>>() {});

        return new EventDocument(
                evt.getId().value(),
                evt.getAggregateId().value(),
                evt.getType(),
                payloadMap,
                evt.getOccurredAt(),
                evt.getVersion()
        );
    }

    public Event<?> toDomain(EventDocument doc) {
        EventId    id        = EventId.of(doc.getId());
        DecisionId aggId     = DecisionId.from(doc.getAggregateId());
        Date       occurred  = doc.getOccurredAt();
        int        version   = doc.getVersion();

        switch (doc.getType()) {
            case DecisionTakenEvent.TYPE -> {
                DecisionTakenEventPayload payload =
                        json.convertValue(doc.getPayload(), DecisionTakenEventPayload.class);

                return DecisionTakenEvent.hydrate(id, aggId, payload, occurred, version);
            }

            default -> throw new IllegalArgumentException(
                    "Unknown event type: " + doc.getType()
            );
        }
    }
}
