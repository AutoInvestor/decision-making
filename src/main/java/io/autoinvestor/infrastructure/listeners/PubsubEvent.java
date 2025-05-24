package io.autoinvestor.infrastructure.listeners;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * Immutable DTO that mirrors the structure coming from Pub/Sub.
 * <p>
 * Expected JSON-shape on the wire:
 * {
 *   "aggregate_id": "abcd-1234",
 *   "type": "NEW_LATEST_NEWS",
 *   "payload": {
 *     "assetId": "AAPL",
 *     "feeling": 3
 *   }
 * }
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PubsubEvent {

    private String aggregateId;

    private String type;

    private Map<String, Object> payload;
}
