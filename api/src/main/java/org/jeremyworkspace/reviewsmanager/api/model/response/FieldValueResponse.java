package org.jeremyworkspace.reviewsmanager.api.model.response;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;
import org.jeremyworkspace.reviewsmanager.api.model.FieldValue;

@Data
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = DateFieldValueResponse.class, name = "dateFieldValue"),
        @JsonSubTypes.Type(value = TextFieldValueResponse.class, name = "textFieldValue"),
        @JsonSubTypes.Type(value = ScoreFieldValueResponse.class, name = "scoreFieldValue")
})
public abstract class FieldValueResponse {

    private Long id;

    public FieldValueResponse(FieldValue fieldValue) {
        this.id = fieldValue.getId();
    }

}
