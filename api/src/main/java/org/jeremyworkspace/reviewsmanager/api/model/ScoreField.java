package org.jeremyworkspace.reviewsmanager.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.jeremyworkspace.reviewsmanager.api.model.helper.FieldType;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@Entity
@Table(name = "scorefield")
public class ScoreField extends Field{

    @Column(name="scoremax", nullable=false)
    @Min(value=1, message="{validation.scorefield.value}")
    private Integer scoreMax;

    @Column(name="display_option", nullable=false)
    private Short displayOption;

    @OneToMany(mappedBy = "scoreField", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @JsonIgnoreProperties("scoreField")
    private List<ScoreFieldValue> scoreFieldValues;

    @Transient
    private final FieldType type = FieldType.SCORE;

}
