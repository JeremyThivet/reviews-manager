package org.jeremyworkspace.reviewsmanager.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.jeremyworkspace.reviewsmanager.api.controller.exception.FormatException;
import org.jeremyworkspace.reviewsmanager.api.model.helper.FieldType;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@Entity
@Table(name = "fieldvalue")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class FieldValue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="fieldvalue_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties("fieldValues")
    @JoinColumn(name="entry_id", nullable=false)
    private Entry entry;

    public abstract FieldType getType();

    public abstract Field getField();

    /**
     * Let you set the field value from a String input.
     * @param value
     * @throws FormatException
     */
    public abstract void setValueFromString(String value) throws FormatException;

}
