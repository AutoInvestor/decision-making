package io.autoinvestor.domain.model;

public enum Type {
    BUY,
    SELL,
    HOLD,
    NONE,
    ;

    public static Type from(String decision) {
        return switch (decision) {
            case "BUY" -> BUY;
            case "SELL" -> SELL;
            case "HOLD" -> HOLD;
            case "NONE" -> NONE;
            default -> throw new IllegalArgumentException("Unknown decision: " + decision);
        };
    }

    public static Type calculateFromFeelingAndRiskLevel(Feeling feeling, RiskLevel riskLevel) {
        int f = feeling.value();
        int r = riskLevel.value();

        if (f == 0 || (f == 1 && r >= 2) || (f == 2 && r >= 3) || (f == 3 && r == 4)) {
            return SELL;
        }

        if ((f >= 7 && r == 4) || (f >= 8 && r >= 3) || (f >= 9 && r >= 2) || f == 10) {
            return BUY;
        }

        return HOLD;
    }

}
