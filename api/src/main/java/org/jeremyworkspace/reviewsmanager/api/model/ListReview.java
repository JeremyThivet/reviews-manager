package org.jeremyworkspace.reviewsmanager.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
@Entity
@Table(name="list")
public class ListReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="list_id")
    private Long id;

    @Column(name="list_name", nullable=false)
    @NotEmpty(message = "{validation.list.notEmpty}")
    @Size(min = 1, message = "{validation.list.size}")
    private String listName;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="created_at", nullable=false)
    private Date creationDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="updated_at", nullable=false)
    private Date lastUpdate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties("listReviews")
    @JoinColumn(name="user_id", nullable=false)
    private User owner;

}
