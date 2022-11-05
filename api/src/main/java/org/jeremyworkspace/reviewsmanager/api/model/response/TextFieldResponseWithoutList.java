package org.jeremyworkspace.reviewsmanager.api.model.response;

import lombok.EqualsAndHashCode;
import org.jeremyworkspace.reviewsmanager.api.model.Field;
import org.jeremyworkspace.reviewsmanager.api.model.TextField;

@EqualsAndHashCode
public class TextFieldResponseWithoutList extends FieldResponseWithoutList{
    public TextFieldResponseWithoutList(TextField field) {
        super(field);
    }
}
