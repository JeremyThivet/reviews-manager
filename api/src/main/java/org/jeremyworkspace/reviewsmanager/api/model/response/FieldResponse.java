package org.jeremyworkspace.reviewsmanager.api.model.response;

import lombok.Data;
import org.jeremyworkspace.reviewsmanager.api.model.Field;
import org.jeremyworkspace.reviewsmanager.api.model.helper.FieldType;

@Data
public class FieldResponse {

    private Long id;
    private String fieldName;
    private ListReviewResponseWithoutFields listReviewResponseWithoutFields;
    private FieldType fieldType;

    public FieldResponse(Field field){
        this.id = field.getId();
        this.fieldName = field.getFieldName();
        this.listReviewResponseWithoutFields = new ListReviewResponseWithoutFields(field.getListReview());
        this.fieldType = field.getType();
    }

}
