package io.autoinvestor.domain.model;

public record Feeling(int value) {
    public static final int MIN_VALUE = 0;
    public static final int MAX_VALUE = 10;

    public Feeling {
        if (value < MIN_VALUE || value > MAX_VALUE) {
            throw new IllegalArgumentException("Feeling value must be between 0 and 10");
        }
    }

    public static Feeling empty() {
        return new Feeling(-1);
    }

    public static Feeling of(int feeling) {
        return new Feeling(feeling);
    }
}
