package io.autoinvestor.application;

import java.util.Date;

public record DecisionDTO(
        String decisionId,
        String assetId,
        Date date,
        String type,
        int riskLevel
) {}
