package io.autoinvestor.infrastructure.read_models;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

@Setter
@Getter
@Document(collection = "decisions")
@CompoundIndexes({
    @CompoundIndex(name = "assetId_riskLevel_idx", def = "{'assetId': 1, 'riskLevel': 1}")
})
public class DecisionDocument {
    @Id private String id;

    private String decisionId;
    private String assetId;
    private Date date;
    private String type;
    private int riskLevel;

    public DecisionDocument() {}

    public DecisionDocument(
            String id, String decisionId, String assetId, Date date, String type, int riskLevel) {
        this.id = id;
        this.decisionId = decisionId;
        this.assetId = assetId;
        this.date = date;
        this.type = type;
        this.riskLevel = riskLevel;
    }
}
