package io.autoinvestor.infrastructure.repositories;

import io.autoinvestor.domain.events.Event;
import io.autoinvestor.domain.events.EventStoreRepository;
import io.autoinvestor.domain.model.Decision;
import io.autoinvestor.domain.model.DecisionId;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@Profile("local")
public class InMemoryEventStoreRepository implements EventStoreRepository {
    private final List<Event<?>> eventStore = new ArrayList<>();

    @Override
    public void save(Decision decision) {
        this.eventStore.addAll(decision.getUncommittedEvents());
    }

    @Override
    public Decision get(DecisionId decisionId) {
        List<Event<?>> eventsForAggregate = eventStore.stream()
                .filter(event -> event.getAggregateId().equals(decisionId))
                .collect(Collectors.toList());

        if (eventsForAggregate.isEmpty()) {
            return null;
        }

        return Decision.from(eventsForAggregate);
    }
}
