package io.autoinvestor.domain.events;

import java.util.Date;
import java.util.Map;

public record DecisionTakenEventPayload(String assetId, Date date, String decision, int riskLevel)
        implements EventPayload {

    @Override
    public Map<String, Object> asMap() {
        return Map.of(
                "assetId", assetId,
                "date", date,
                "decision", decision,
                "riskLevel", riskLevel);
    }
}
