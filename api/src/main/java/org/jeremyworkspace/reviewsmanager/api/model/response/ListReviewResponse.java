package org.jeremyworkspace.reviewsmanager.api.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.jeremyworkspace.reviewsmanager.api.model.ListReview;
import org.jeremyworkspace.reviewsmanager.api.model.User;

import javax.persistence.*;
import java.util.Date;

/**
 * This class is used to return a JSON object without the user password.
 */
@Data
public class ListReviewResponse {

    private Long id;
    private String listName;
    private Date creationDate;
    private Date lastUpdate;
    private UserResponse owner;

    public ListReviewResponse(ListReview list){
        this.owner = new UserResponse(list.getOwner());
        this.id = list.getId();
        this.listName = list.getListName();
        this.creationDate = list.getCreationDate();
        this.lastUpdate = list.getLastUpdate();
    }
}
