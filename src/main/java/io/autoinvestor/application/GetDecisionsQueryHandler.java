package io.autoinvestor.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

@Service
public class GetDecisionsQueryHandler {

    private final ReadModelRepository readModel;

    public GetDecisionsQueryHandler(ReadModelRepository readModel) {
        this.readModel = readModel;
    }

    public List<GetDecisionsQueryResponse> handle(GetDecisionsQuery query) {
        List<DecisionDTO> decisions = this.readModel.get(query.assetId(), query.riskLevel());

        return decisions.stream()
                .map(
                        d ->
                                new GetDecisionsQueryResponse(
                                        d.assetId(), d.type(), d.date(), d.riskLevel()))
                .collect(Collectors.toList());
    }
}
