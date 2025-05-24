package io.autoinvestor.ui;

import io.autoinvestor.application.GetDecisionsQuery;
import io.autoinvestor.application.GetDecisionsQueryHandler;
import io.autoinvestor.application.GetDecisionsQueryResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/decisions")
public class GetDecisionsController {

    private final GetDecisionsQueryHandler handler;

    public GetDecisionsController(GetDecisionsQueryHandler handler) {
        this.handler = handler;
    }

    @GetMapping
    public ResponseEntity<List<GetDecisionsDTO>> getDecisions(
            @RequestParam String assetId,
            @RequestParam Integer riskLevel) {

        List<GetDecisionsQueryResponse> queryResponse = this.handler.handle(
                new GetDecisionsQuery(assetId, riskLevel)
        );

        List<GetDecisionsDTO> dto = queryResponse.stream()
                .map(d -> new GetDecisionsDTO(
                        d.assetId(),
                        d.type(),
                        d.date(),
                        d.riskLevel()
                ))
                .toList();

        return ResponseEntity.ok(dto);
    }
}
