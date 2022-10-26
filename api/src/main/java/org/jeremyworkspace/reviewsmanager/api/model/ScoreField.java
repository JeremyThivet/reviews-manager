package org.jeremyworkspace.reviewsmanager.api.model;

import lombok.Data;
import org.jeremyworkspace.reviewsmanager.api.model.helper.FieldType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Entity
@Table(name = "scorefield")
public class ScoreField extends Field{

    @Column(name="scoremax", nullable=false)
    @Min(value=1, message="{validation.scorefield.value}")
    private Integer scoreMax;

    @Column(name="display_option", nullable=false)
    private Short displayOption;

    @Transient
    private final FieldType type = FieldType.SCORE;

}
