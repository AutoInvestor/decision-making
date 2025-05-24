package io.autoinvestor.infrastructure.read_models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "decisions")
@CompoundIndexes({
        @CompoundIndex(name = "assetId_riskLevel_idx",
                def = "{'assetId': 1, 'riskLevel': 1}")
})
public class DecisionDocument {
    @Id
    private String id;

    private String decisionId;
    private String assetId;
    private Date   date;
    private String type;
    private int    riskLevel;

    public DecisionDocument() { }

    public DecisionDocument(String id,
                            String decisionId,
                            String assetId,
                            Date date,
                            String type,
                            int riskLevel) {
        this.id        = id;
        this.decisionId = decisionId;
        this.assetId   = assetId;
        this.date      = date;
        this.type      = type;
        this.riskLevel = riskLevel;
    }

    public String getId()        { return id; }
    public String getDecisionId() { return decisionId; }
    public String getAssetId()   { return assetId; }
    public Date   getDate()      { return date; }
    public String getType()      { return type; }
    public int    getRiskLevel() { return riskLevel; }

    public void setId(String id)               { this.id = id; }
    public void setDecisionId(String decisionId) { this.decisionId = decisionId; }
    public void setAssetId(String assetId)     { this.assetId = assetId; }
    public void setDate(Date date)             { this.date = date; }
    public void setType(String type)           { this.type = type; }
    public void setRiskLevel(int riskLevel)    { this.riskLevel = riskLevel; }
}
