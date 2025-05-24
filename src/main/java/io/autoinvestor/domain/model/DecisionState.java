package io.autoinvestor.domain.model;

import io.autoinvestor.domain.events.DecisionTakenEvent;

import java.util.Date;

public class DecisionState {
    private final DecisionId decisionId;
    private final AssetId assetId;
    private final Date date;
    private final Type type;
    private final RiskLevel riskLevel;


    private DecisionState(DecisionId decisionId, AssetId assetId, Date date, Type type, RiskLevel riskLevel) {
        this.decisionId = decisionId;
        this.assetId = assetId;
        this.date = date;
        this.type = type;
        this.riskLevel = riskLevel;
    }

    public static DecisionState empty() {
        return new DecisionState(DecisionId.generate(), AssetId.generate(), new Date() ,Type.NONE, RiskLevel.empty());
    }

    public String assetId() {
        return assetId.value();
    }

    public DecisionId decisionId() {
        return decisionId;
    }

    public Type type() {
        return type;
    }

    public Date date() {
        return date;
    }

    public String decision() {
        return type.name();
    }

    public int riskLevel() {
        return riskLevel.value();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        DecisionState that = (DecisionState) obj;
        return decisionId.equals(that.decisionId) &&
                assetId.equals(that.assetId) &&
                type == that.type &&
                riskLevel.equals(that.riskLevel);
    }

    public DecisionState withDecisionTaken(DecisionTakenEvent event) {
        return new DecisionState(
                DecisionId.from(event.getId().value()),
                AssetId.of(event.getPayload().assetId()),
                event.getPayload().date(),
                Type.from(event.getPayload().decision()),
                RiskLevel.of(event.getPayload().riskLevel()));
    }
}
