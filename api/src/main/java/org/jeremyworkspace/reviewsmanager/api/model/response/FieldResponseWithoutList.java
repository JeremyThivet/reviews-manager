package org.jeremyworkspace.reviewsmanager.api.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;
import org.jeremyworkspace.reviewsmanager.api.model.Field;
import org.jeremyworkspace.reviewsmanager.api.model.helper.FieldType;

@Data
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = ScoreFieldResponseWithoutList.class, name = "score"),
        @JsonSubTypes.Type(value = TextFieldResponseWithoutList.class, name = "text"),
        @JsonSubTypes.Type(value = DateFieldResponseWithoutList.class, name = "date")
})
public abstract class FieldResponseWithoutList {

    private Long id;
    private String fieldName;
    private FieldType fieldType;

    public FieldResponseWithoutList(Field field){
        this.id = field.getId();
        this.fieldName = field.getFieldName();
        this.fieldType = field.getType();
    }

}
