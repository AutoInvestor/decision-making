package io.autoinvestor.domain.events;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import java.util.Map;

public record DecisionTakenEventPayload(
        String assetId,
        @JsonFormat(
                shape    = JsonFormat.Shape.STRING,
                pattern  = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX",
                timezone = "UTC"
        )
        Date date,
        String decision,
        int riskLevel
) implements EventPayload {

    @Override
    public Map<String, Object> asMap() {
        return Map.of(
                "assetId", assetId,
                "date", date,
                "decision", decision,
                "riskLevel", riskLevel
        );
    }
}
