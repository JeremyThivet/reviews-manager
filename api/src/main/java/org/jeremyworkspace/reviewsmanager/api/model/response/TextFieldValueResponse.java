package org.jeremyworkspace.reviewsmanager.api.model.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.jeremyworkspace.reviewsmanager.api.model.TextFieldValue;

@Getter
@Setter
@EqualsAndHashCode
public class TextFieldValueResponse extends FieldValueResponse {

    private String value;

    private TextFieldResponseWithoutList field;

    public TextFieldValueResponse(TextFieldValue fieldValue) {
        super(fieldValue);
        value = fieldValue.getText();
        this.field = new TextFieldResponseWithoutList(fieldValue.getTextField());
    }
}
