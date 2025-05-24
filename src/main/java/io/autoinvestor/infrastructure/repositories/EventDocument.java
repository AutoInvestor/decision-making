package io.autoinvestor.infrastructure.repositories;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Getter
@Setter
@Document(collection = "events")
public class EventDocument {

    @Id          private String id;
    @Field       private String aggregateId;
    @Field       private String type;
    @Field       private JsonNode payload;
    @Field       private Date occurredAt;
    @Field       private int version;

    public EventDocument() {}

    public EventDocument(String id,
                         String aggregateId,
                         String type,
                         JsonNode payload,
                         Date occurredAt,
                         int version) {
        this.id          = id;
        this.aggregateId = aggregateId;
        this.type        = type;
        this.payload     = payload;
        this.occurredAt  = occurredAt;
        this.version     = version;
    }
}
