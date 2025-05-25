package io.autoinvestor.infrastructure.repositories;

import io.autoinvestor.domain.events.Event;
import io.autoinvestor.domain.events.EventStoreRepository;
import io.autoinvestor.domain.model.Decision;
import io.autoinvestor.domain.model.DecisionId;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@Profile("prod")
public class MongoEventStoreRepository implements EventStoreRepository {
    private static final String COLLECTION = "events";

    private final MongoTemplate template;
    private final EventMapper mapper;

    public MongoEventStoreRepository(MongoTemplate template, EventMapper mapper) {
        this.template = template;
        this.mapper = mapper;
    }

    @Override
    public void save(Decision decision) {
        List<EventDocument> docs = decision.getUncommittedEvents()
                .stream()
                .map(mapper::toDocument)
                .collect(Collectors.toList());
        template.insertAll(docs);
    }

    @Override
    public Decision get(DecisionId decisionId) {
        Query q = Query.query(
                        Criteria.where("decisionId")
                                .is(decisionId.toString())
                )
                .with(Sort.by("version"));

        List<EventDocument> docs = template.find(q, EventDocument.class, COLLECTION);

        if (docs.isEmpty()) {
            return null;
        }

        List<Event<?>> events = docs.stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());

        if (events.isEmpty()) {
            return Decision.empty();
        }

        return Decision.from(events);
    }
}
