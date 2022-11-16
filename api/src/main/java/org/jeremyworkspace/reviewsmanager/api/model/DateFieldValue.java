package org.jeremyworkspace.reviewsmanager.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.jeremyworkspace.reviewsmanager.api.model.helper.FieldType;

import javax.persistence.*;
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
}
