package org.jeremyworkspace.reviewsmanager.api.model.response;

import org.jeremyworkspace.reviewsmanager.api.controller.exception.FormatException;
import org.jeremyworkspace.reviewsmanager.api.model.*;
import org.jeremyworkspace.reviewsmanager.api.model.helper.FieldType;

public class FieldResponseWithoutListFactory {

    public static FieldResponseWithoutList getFieldResponseWithoutList(Field field) throws FormatException {
        FieldResponseWithoutList res = null;
        FieldType fieldType = field.getType();

        if (fieldType == FieldType.TEXT) {
            res = new TextFieldResponseWithoutList((TextField) field);
        } else if (fieldType == FieldType.DATE) {
            res = new DateFieldResponseWithoutList((DateField) field);
        } else if (fieldType == FieldType.SCORE) {
            res = new ScoreFieldResponseWithoutList((ScoreField) field);
        } else {
            throw new FormatException("Mauvais type de champ personnalisé");
        }

        return res;
    }
}
