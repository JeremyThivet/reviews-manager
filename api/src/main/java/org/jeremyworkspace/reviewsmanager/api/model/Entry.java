package org.jeremyworkspace.reviewsmanager.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name="entry")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Entry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="entry_id")
    private Long id;

    @Column(name="entry_name", nullable=false)
    private String entryName;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="created_at", nullable=false)
    private Date creationDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="updated_at", nullable=false)
    private Date lastUpdate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"entries", "listReviews"})
    @JoinColumn(name="user_id", nullable=false)
    private User creator;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties("entries")
    @JoinColumn(name="list_id", nullable=false)
    private ListReview listReview;

    @OneToMany(mappedBy = "entry", cascade = {CascadeType.REMOVE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
    @JsonIgnoreProperties("entry")
    private List<FieldValue> fieldValues;

}
