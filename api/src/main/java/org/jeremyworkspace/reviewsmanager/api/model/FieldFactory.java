package org.jeremyworkspace.reviewsmanager.api.model;

import org.jeremyworkspace.reviewsmanager.api.controller.exception.FormatException;
import org.jeremyworkspace.reviewsmanager.api.model.helper.FieldType;

public class FieldFactory {

    public static Class<? extends Field> getClassFromType(FieldType type) throws FormatException {
        if(type == FieldType.TEXT)
            return TextField.class;
        if(type == FieldType.SCORE)
            return ScoreField.class;
        if(type == FieldType.DATE)
            return DateField.class;

        throw new FormatException("Wrong type of field");
    }

}
