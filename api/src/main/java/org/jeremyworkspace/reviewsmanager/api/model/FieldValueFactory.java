package org.jeremyworkspace.reviewsmanager.api.model;

import org.jeremyworkspace.reviewsmanager.api.controller.exception.FormatException;
import org.jeremyworkspace.reviewsmanager.api.model.helper.FieldType;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This factory is used to get the adapted FieldValue depending on the field type.
 */
public class FieldValueFactory {

    private static final String TEXT_DEFAULT = "A renseigner";
    private static final Integer SCORE_DEFAULT = 0;
    private static final Date DATE_DEFAULT = new Date();

    public static FieldValue getFieldValue(Field field, String value, boolean defaultValue) throws FormatException {

        FieldValue res = null;
        FieldType fieldType = field.getType();

        if (fieldType == FieldType.TEXT) {
            res = new TextFieldValue(defaultValue ? TEXT_DEFAULT : value, (TextField) field);
        }
        else if (fieldType == FieldType.DATE) {
            Date date = null;
            // Date is given with the following format : yyyy-MM-dd'T'HH:mm:ss.SSSXXX
            try {
                date = defaultValue ? new Date() : new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX").parse(value);
            } catch (ParseException e){
                throw new FormatException("Mauvais format de date.", e);
            }
            res = new DateFieldValue(defaultValue ? DATE_DEFAULT : date, (DateField) field);
        }
        else if (fieldType == FieldType.SCORE) {
            res = new ScoreFieldValue(defaultValue ? SCORE_DEFAULT : Integer.parseInt(value), (ScoreField) field);
        } else {
            throw new FormatException("Mauvais type de champ personnalisé");
        }

        return res;
    }

}
