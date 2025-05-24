package io.autoinvestor.infrastructure.read_models;

import io.autoinvestor.application.DecisionDTO;
import io.autoinvestor.application.ReadModelRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
@Profile("local")
public class InMemoryReadModelRepository implements ReadModelRepository {
    private final List<DecisionDTO> decisions = List.of(
            new DecisionDTO("12345", "AAPL", new Date(), "BUY", 1),
            new DecisionDTO("54321", "AAPL", new Date(), "SELL", 2),
            new DecisionDTO("21345", "GOOGL", new Date(), "BUY", 3)
    );

    @Override
    public void save(DecisionDTO decisionDTO) {
        decisions.add(decisionDTO);
    }

    @Override
    public List<DecisionDTO> get(String assetId, int riskLevel) {
        return decisions.stream()
                .filter(decision -> decision.assetId().equals(assetId) && decision.riskLevel() == riskLevel)
                .toList();
    }

    @Override
    public Optional<DecisionDTO> getOne(String assetId, int riskLevel) {
        return decisions.stream()
                .filter(decision -> decision.assetId().equals(assetId) && decision.riskLevel() == riskLevel)
                .findFirst();
    }
}
