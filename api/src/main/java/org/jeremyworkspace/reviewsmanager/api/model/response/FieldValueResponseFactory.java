package org.jeremyworkspace.reviewsmanager.api.model.response;

import org.jeremyworkspace.reviewsmanager.api.controller.exception.FormatException;
import org.jeremyworkspace.reviewsmanager.api.model.*;
import org.jeremyworkspace.reviewsmanager.api.model.helper.FieldType;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FieldValueResponseFactory {

    public static FieldValueResponse getFieldValueResponse(FieldValue fieldValue) throws FormatException {
        FieldValueResponse res = null;
        FieldType fieldType = fieldValue.getType();

        if (fieldType == FieldType.TEXT) {
            res = new TextFieldValueResponse((TextFieldValue) fieldValue);
        }
        else if (fieldType == FieldType.DATE) {
            res = new DateFieldValueResponse((DateFieldValue) fieldValue);
        }
        else if (fieldType == FieldType.SCORE) {
            res = new ScoreFieldValueResponse((ScoreFieldValue) fieldValue);
        } else {
            throw new FormatException("Mauvais type de champ personnalisé");
        }

        return res;
    }


}
