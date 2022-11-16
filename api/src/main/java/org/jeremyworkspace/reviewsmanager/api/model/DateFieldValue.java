package org.jeremyworkspace.reviewsmanager.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.jeremyworkspace.reviewsmanager.api.controller.exception.FormatException;
import org.jeremyworkspace.reviewsmanager.api.model.helper.FieldType;

import javax.persistence.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


@Data
@Entity
@Table(name = "datefieldvalue")
public class DateFieldValue extends FieldValue {

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="date", nullable=false)
    private Date date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="field_id", nullable=false)
    @JsonIgnoreProperties("dateFieldValues")
    private DateField dateField;


    public DateFieldValue(Date d, DateField field){
        super();
        this.date = d;
        this.dateField = field;
    }

    public DateFieldValue(){
        super();
    }

    @Override
    public FieldType getType() {
        return FieldType.DATE;
    }

    @Override
    public Field getField() {
        return this.dateField;
    }

    @Override
    public void setValueFromString(String value) throws FormatException {
        try {
            Date d = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX").parse(value);
            this.date = d;
        } catch (ParseException e){
            throw new FormatException("Mauvais format de date.", e);
        }
    }


}
