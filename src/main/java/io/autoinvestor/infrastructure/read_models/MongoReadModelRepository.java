package io.autoinvestor.infrastructure.read_models;

import io.autoinvestor.application.DecisionDTO;
import io.autoinvestor.application.ReadModelRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.core.index.IndexOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository
@Profile("prod")
public class MongoReadModelRepository implements ReadModelRepository {

    private static final String COLLECTION = "decisions";

    private final MongoTemplate template;
    private final DecisionMapper mapper;

    public MongoReadModelRepository(MongoTemplate template, DecisionMapper mapper) {
        this.template = template;
        this.mapper = mapper;

        IndexOperations indexOps = template.indexOps(COLLECTION);
        Index compound =
                new Index()
                        .on("assetId", Sort.Direction.ASC)
                        .on("riskLevel", Sort.Direction.ASC)
                        .named("assetId_riskLevel_idx");
        indexOps.ensureIndex(compound);
    }

    @Override
    public void save(DecisionDTO decisionDTO) {
        template.save(mapper.toDocument(decisionDTO), COLLECTION);
    }

    @Override
    public List<DecisionDTO> get(String assetId, int riskLevel) {
        Query query =
                Query.query(Criteria.where("assetId").is(assetId).and("riskLevel").is(riskLevel));
        return template.find(query, DecisionDocument.class, COLLECTION).stream()
                .map(mapper::toDTO)
                .toList();
    }

    @Override
    public Optional<DecisionDTO> getOne(String assetId, int riskLevel) {
        Query query =
                Query.query(Criteria.where("assetId").is(assetId).and("riskLevel").is(riskLevel));
        DecisionDocument doc = template.findOne(query, DecisionDocument.class, COLLECTION);
        return Optional.ofNullable(doc).map(mapper::toDTO);
    }
}
