package org.jeremyworkspace.reviewsmanager.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.jeremyworkspace.reviewsmanager.api.model.helper.FieldType;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@Entity
@Table(name = "field")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Field {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="field_id")
    private Long id;

    @Column(name="field_name", nullable=false)
    @NotEmpty(message = "{validation.field.notEmpty}")
    @Size(min = 1, max=40, message = "{validation.field.size}")
    private String fieldName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties("fields")
    @JoinColumn(name="list_id", nullable=false)
    private ListReview listReview;

    @Transient
    private final FieldType type = FieldType.NOTYPE;

}
