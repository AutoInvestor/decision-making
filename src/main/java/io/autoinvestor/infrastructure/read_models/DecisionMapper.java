package io.autoinvestor.infrastructure.read_models;

import io.autoinvestor.application.DecisionDTO;
import org.springframework.stereotype.Component;


@Component
public class DecisionMapper {

    public DecisionDocument toDocument(DecisionDTO dto) {
        return new DecisionDocument(
                null,
                dto.decisionId(),
                dto.assetId(),
                dto.date(),
                dto.type(),
                dto.riskLevel()
        );
    }

    public DecisionDTO toDTO(DecisionDocument doc) {
        return new DecisionDTO(
                doc.getDecisionId(),
                doc.getAssetId(),
                doc.getDate(),
                doc.getType(),
                doc.getRiskLevel()
        );
    }
}
