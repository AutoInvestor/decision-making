package io.autoinvestor.application;

import io.autoinvestor.domain.events.Event;
import io.autoinvestor.domain.events.EventPublisher;
import io.autoinvestor.domain.events.EventStoreRepository;
import io.autoinvestor.domain.model.Decision;
import io.autoinvestor.domain.model.DecisionId;
import io.autoinvestor.domain.model.RiskLevel;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RegisterDecisionCommandHandler {

    private final EventStoreRepository eventStore;
    private final ReadModelRepository readModel;
    private final EventPublisher eventPublisher;

    public RegisterDecisionCommandHandler(EventStoreRepository eventStore, ReadModelRepository readModel, EventPublisher eventPublisher) {
        this.eventStore = eventStore;
        this.readModel = readModel;
        this.eventPublisher = eventPublisher;
    }

    public void handle(RegisterDecisionCommand command) {
        for (RiskLevel risklevel : RiskLevel.values()) {
            Optional<DecisionDTO> decisionFromReadModel = this.readModel.getOne(command.assetId(), risklevel.value());

            Decision decision = decisionFromReadModel.isEmpty()
                    ? Decision.empty()
                    : this.eventStore.get(DecisionId.from(decisionFromReadModel.get().decisionId()));

            if (decision == null) {
                throw new IllegalStateException("Decision not found for ID: " + command.assetId() + " when it should be present.");
            }

            decision.takeDecision(
                    command.assetId(),
                    command.feeling(),
                    risklevel
            );

            List<Event<?>> events = decision.getUncommittedEvents();

            this.eventStore.save(decision);

            DecisionDTO dto = new DecisionDTO(
                    decision.getState().decisionId().value(),
                    command.assetId(),
                    decision.getState().date(),
                    decision.getState().decision(),
                    risklevel.value()
            );
            this.readModel.save(dto);

            this.eventPublisher.publish(events);

            decision.markEventsAsCommitted();
        }
    }
}
