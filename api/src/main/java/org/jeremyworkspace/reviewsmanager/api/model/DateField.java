package org.jeremyworkspace.reviewsmanager.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.jeremyworkspace.reviewsmanager.api.model.helper.FieldType;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "datefield")
public class DateField extends Field {

    @OneToMany(mappedBy = "dateField", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @JsonIgnoreProperties("dateField")
    private List<DateFieldValue> dateFieldValues;

    @Transient
    private final FieldType type = FieldType.DATE;
}
