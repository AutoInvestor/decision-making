package io.autoinvestor.infrastructure.repositories;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;


@Document(collection = "events")
public class EventDocument {

    @Id
    private String id;

    private String aggregateId;
    private String type;
    private String payload;
    private Date   occurredAt;
    private int    version;

    public EventDocument() {}

    public EventDocument(String id,
                         String aggregateId,
                         String type,
                         String payload,
                         Date occurredAt,
                         int version) {
        this.id          = id;
        this.aggregateId = aggregateId;
        this.type        = type;
        this.payload     = payload;
        this.occurredAt  = occurredAt;
        this.version     = version;
    }

    public String getId() {
        return id;
    }

    public String getAggregateId() {
        return aggregateId;
    }

    public String getType() {
        return type;
    }

    public String getPayload() {
        return payload;
    }

    public Date getOccurredAt() {
        return occurredAt;
    }

    public int getVersion() {
        return version;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setAggregateId(String aggregateId) {
        this.aggregateId = aggregateId;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public void setOccurredAt(Date occurredAt) {
        this.occurredAt = occurredAt;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
