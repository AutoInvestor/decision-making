package io.autoinvestor.ui;

import java.util.Date;

public record GetDecisionsDTO(String assetId, String type, Date date, int riskLevel) {}
