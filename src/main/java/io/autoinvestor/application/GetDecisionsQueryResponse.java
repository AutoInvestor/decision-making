package io.autoinvestor.application;

import java.util.Date;

public record GetDecisionsQueryResponse(
        String assetId,
        String type,
        Date date,
        int riskLevel
) { }