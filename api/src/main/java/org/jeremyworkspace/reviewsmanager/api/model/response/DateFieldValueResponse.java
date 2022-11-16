package org.jeremyworkspace.reviewsmanager.api.model.response;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.jeremyworkspace.reviewsmanager.api.model.DateFieldValue;

import java.util.Date;

@Getter
@Setter
@EqualsAndHashCode
public class DateFieldValueResponse extends FieldValueResponse{

    private Date value;

    private DateFieldResponseWithoutList field;

    public DateFieldValueResponse(DateFieldValue fieldValue) {
        super(fieldValue);
        this.value = fieldValue.getDate();
        this.field = new DateFieldResponseWithoutList(fieldValue.getDateField());
    }
}
