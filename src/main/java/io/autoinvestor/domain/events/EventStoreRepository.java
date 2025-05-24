package io.autoinvestor.domain.events;

import io.autoinvestor.domain.model.Decision;
import io.autoinvestor.domain.model.DecisionId;


public interface EventStoreRepository {
    Decision get(DecisionId decisionId);
    void save(Decision decision);
}
