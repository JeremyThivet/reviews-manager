package org.jeremyworkspace.reviewsmanager.api.model.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.jeremyworkspace.reviewsmanager.api.model.Field;
import org.jeremyworkspace.reviewsmanager.api.model.ScoreField;
import org.jeremyworkspace.reviewsmanager.api.model.TextField;

@Getter
@Setter
@EqualsAndHashCode
public class ScoreFieldResponseWithoutList extends FieldResponseWithoutList{

    private Integer scoreMax;
    private Short displayOption;

    public ScoreFieldResponseWithoutList(ScoreField field) {
        super(field);
        this.scoreMax = field.getScoreMax();
        this.displayOption = field.getDisplayOption();
    }



}
