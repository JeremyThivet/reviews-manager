package org.jeremyworkspace.reviewsmanager.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.jeremyworkspace.reviewsmanager.api.model.helper.FieldType;

import javax.persistence.*;


@Data
@Entity
@Table(name = "textfieldvalue")
public class TextFieldValue extends FieldValue {

    @Column(name="text", nullable=false)
    private String text;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="field_id", nullable=false)
    @JsonIgnoreProperties("textFieldValues")
    private TextField textField;

    public TextFieldValue(String text, TextField tf){
        super();
        this.text = text;
        this.textField = tf;
    }

    public TextFieldValue(){
        super();
    }

    @Override
    public FieldType getType() {
        return FieldType.TEXT;
    }

    public Field getField() {
        return this.textField;
    }

    @Override
    public void setValueFromString(String value) {
        this.text = value;
    }
}
