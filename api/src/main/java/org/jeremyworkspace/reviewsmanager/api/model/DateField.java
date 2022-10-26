package org.jeremyworkspace.reviewsmanager.api.model;

import lombok.Data;
import org.jeremyworkspace.reviewsmanager.api.model.helper.FieldType;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

@Data
@Entity
@Table(name = "datefield")
public class DateField extends Field {

    @Transient
    private final FieldType type = FieldType.DATE;
}
