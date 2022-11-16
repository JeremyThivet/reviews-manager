package org.jeremyworkspace.reviewsmanager.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.jeremyworkspace.reviewsmanager.api.model.helper.FieldType;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "textfield")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class TextField extends Field {

    @OneToMany(mappedBy = "textField", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @JsonIgnoreProperties("textField")
    private List<TextFieldValue> textFieldValues;

    @Transient
    private final FieldType type = FieldType.TEXT;
}
