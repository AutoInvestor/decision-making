package io.autoinvestor.domain.model;

import com.fasterxml.jackson.annotation.JsonValue;

public record RiskLevel(@JsonValue int value) {
    public static final RiskLevel R1 = new RiskLevel(1);
    public static final RiskLevel R2 = new RiskLevel(2);
    public static final RiskLevel R3 = new RiskLevel(3);
    public static final RiskLevel R4 = new RiskLevel(4);
    public static final RiskLevel NONE = new RiskLevel(-1);

    public static RiskLevel empty() {
        return NONE;
    }

    public static RiskLevel of(int riskLevel) {
        return switch (riskLevel) {
            case 1 -> R1;
            case 2 -> R2;
            case 3 -> R3;
            case 4 -> R4;
            default -> throw new IllegalArgumentException("Unknown risk level: " + riskLevel);
        };
    }

    public static RiskLevel[] values() {
        return new RiskLevel[] {R1, R2, R3, R4};
    }
}
