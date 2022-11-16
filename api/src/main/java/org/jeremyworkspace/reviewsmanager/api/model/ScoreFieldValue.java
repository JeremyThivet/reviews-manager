package org.jeremyworkspace.reviewsmanager.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.jeremyworkspace.reviewsmanager.api.controller.exception.FormatException;
import org.jeremyworkspace.reviewsmanager.api.model.helper.FieldType;

import javax.persistence.*;

@Data
@Entity
@Table(name = "scorefieldvalue")
public class ScoreFieldValue extends FieldValue {

    @Column(name="score", nullable=false)
    private Integer score;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="field_id", nullable=false)
    @JsonIgnoreProperties("scoreFieldValues")
    private ScoreField scoreField;

    public ScoreFieldValue(Integer score, ScoreField scoreField) throws FormatException {
        super();

        if(score > scoreField.getScoreMax()){
            throw new FormatException("Le score est plus élevé que le score maximum spécifié dans le champ.");
        }
        this.score = score;
        this.scoreField = scoreField;
    }

    public ScoreFieldValue(){
        super();
    }

    @Override
    public FieldType getType() {
        return FieldType.SCORE;
    }

    public Field getField() {
        return this.scoreField;
    }

    @Override
    public void setValueFromString(String value) throws FormatException {
        try {
            int score = Integer.parseInt(value);
            if(score > scoreField.getScoreMax()){
                throw new FormatException("Le score est plus élevé que le score maximum spécifié dans le champ.");
            }
            this.score = score;
        } catch (NumberFormatException e){
            throw new FormatException("Mauvais format d'entier.", e);
        }
    }
}