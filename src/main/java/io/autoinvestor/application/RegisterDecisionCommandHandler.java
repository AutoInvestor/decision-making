package io.autoinvestor.application;

import io.autoinvestor.domain.events.EventStoreRepository;
import io.autoinvestor.domain.model.Decision;
import io.autoinvestor.domain.model.DecisionId;
import io.autoinvestor.domain.model.RiskLevel;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RegisterDecisionCommandHandler {

    private final EventStoreRepository eventStore;
    private final ReadModelRepository readModel;

    public RegisterDecisionCommandHandler(EventStoreRepository eventStore, ReadModelRepository readModel) {
        this.eventStore = eventStore;
        this.readModel = readModel;
    }

    public void handle(RegisterDecisionCommand command) {
        for (RiskLevel risklevel : RiskLevel.values()) {
            Optional<DecisionDTO> decisionFromReadModel = this.readModel.getOne(command.assetId(), risklevel.value());

            Decision decision = decisionFromReadModel.isEmpty()
                    ? Decision.empty()
                    : this.eventStore.get(DecisionId.from(decisionFromReadModel.get().decisionId()));

            decision.takeDecision(
                    command.assetId(),
                    command.feeling(),
                    risklevel
            );

            this.eventStore.save(decision);

            DecisionDTO dto = new DecisionDTO(
                    decision.getState().decisionId().value(),
                    command.assetId(),
                    decision.getState().date(),
                    decision.getState().decision(),
                    risklevel.value()
            );
            this.readModel.save(dto);

            decision.markEventsAsCommitted();
        }
    }
}
