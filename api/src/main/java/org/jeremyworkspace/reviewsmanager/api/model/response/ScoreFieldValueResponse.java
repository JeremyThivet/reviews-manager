package org.jeremyworkspace.reviewsmanager.api.model.response;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.jeremyworkspace.reviewsmanager.api.model.FieldValue;
import org.jeremyworkspace.reviewsmanager.api.model.ScoreFieldValue;

@Getter
@Setter
@EqualsAndHashCode
public class ScoreFieldValueResponse extends FieldValueResponse{

    private Integer value;

    private ScoreFieldResponseWithoutList field;

    public ScoreFieldValueResponse(ScoreFieldValue fieldValue) {
        super(fieldValue);
        this.value = fieldValue.getScore();
        this.field = new ScoreFieldResponseWithoutList(fieldValue.getScoreField());
    }
}
