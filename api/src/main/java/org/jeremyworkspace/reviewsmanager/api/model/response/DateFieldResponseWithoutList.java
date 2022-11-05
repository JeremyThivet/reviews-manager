package org.jeremyworkspace.reviewsmanager.api.model.response;

import lombok.EqualsAndHashCode;
import org.jeremyworkspace.reviewsmanager.api.model.DateField;
import org.jeremyworkspace.reviewsmanager.api.model.Field;

@EqualsAndHashCode
public class DateFieldResponseWithoutList extends FieldResponseWithoutList{
    public DateFieldResponseWithoutList(DateField field) {
        super(field);
    }
}
