package io.autoinvestor.domain.model;

import io.autoinvestor.domain.Id;

public class DecisionId extends Id {
    DecisionId(String id) {
        super(id);
    }

    public static DecisionId generate() {
        return new DecisionId(generateId());
    }

    public static DecisionId from(String id) {
        return new DecisionId(id);
    }
}
