package io.autoinvestor.application;

import java.util.List;
import java.util.Optional;

public interface ReadModelRepository {
    void save(DecisionDTO decisionDTO);
    List<DecisionDTO> get(String assetId, int riskLevel);
    Optional<DecisionDTO> getOne(String assetId, int riskLevel);
}
