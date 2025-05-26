package io.autoinvestor.domain.model;

import io.autoinvestor.domain.events.DecisionTakenEvent;
import io.autoinvestor.domain.events.Event;
import io.autoinvestor.domain.events.EventSourcedEntity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Decision extends EventSourcedEntity {
    private DecisionState state;

    private Decision(List<Event<?>> stream) {
        super(stream);

        if (stream.isEmpty()) {
            this.state = DecisionState.empty();
        }
    }

    public static Decision empty() {
        return new Decision(new ArrayList<>());
    }

    public static Decision from(List<Event<?>> stream) {
        return new Decision(stream);
    }

    public void takeDecision(String assetId, int feelingInt, RiskLevel riskLevel) {
        Feeling feeling = Feeling.of(feelingInt);
        Type type = Type.calculateFromFeelingAndRiskLevel(feeling, riskLevel);

        this.apply(DecisionTakenEvent.with(
                this.state.decisionId(),
                AssetId.of(assetId),
                new Date(),
                type,
                riskLevel
        ));
    }

    @Override
    protected void when(Event<?> e) {
        switch (e.getType()) {
            case DecisionTakenEvent.TYPE:
                whenDecisionTaken((DecisionTakenEvent) e);
                break;
            default:
                throw new IllegalArgumentException("Unknown event type");
        }
    }

    private void whenDecisionTaken(DecisionTakenEvent event) {
        if (this.state == null) {
            this.state = DecisionState.empty();
        }
        this.state = this.state.withDecisionTaken(event);
    }

    public DecisionState getState() {
        return state;
    }
}
