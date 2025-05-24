package io.autoinvestor.domain.events;

import io.autoinvestor.domain.Id;
import io.autoinvestor.domain.model.*;

import java.util.Date;


public class DecisionTakenEvent extends Event<DecisionTakenEventPayload> {

    public static final String TYPE = "ASSET_DECISION_TAKEN";

    private DecisionTakenEvent(Id aggregateId, DecisionTakenEventPayload payload) {
        super(aggregateId, TYPE, payload);
    }

    protected DecisionTakenEvent(EventId id,
                                 DecisionId decisionId,
                                 DecisionTakenEventPayload payload,
                                 Date occurredAt,
                                 int version) {
        super(id, decisionId, TYPE, payload, occurredAt, version);
    }

    public static DecisionTakenEvent with(DecisionId decisionId, AssetId assetId, Date date, Type type, RiskLevel riskLevel) {
        DecisionTakenEventPayload payload = new DecisionTakenEventPayload(
                assetId.value(),
                date,
                type.name(),
                riskLevel.value()
        );
        return new DecisionTakenEvent(decisionId, payload);
    }

    public static DecisionTakenEvent hydrate(EventId id,
                                             DecisionId decisionId,
                                             DecisionTakenEventPayload payload,
                                             Date occurredAt,
                                             int version) {
        return new DecisionTakenEvent(id, decisionId, payload, occurredAt, version);
    }
}
