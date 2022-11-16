package org.jeremyworkspace.reviewsmanager.api.model.response;

import lombok.Data;
import org.jeremyworkspace.reviewsmanager.api.model.DateField;
import org.jeremyworkspace.reviewsmanager.api.model.ListReview;
import org.jeremyworkspace.reviewsmanager.api.model.ScoreField;
import org.jeremyworkspace.reviewsmanager.api.model.TextField;
import org.jeremyworkspace.reviewsmanager.api.model.helper.FieldType;


import java.util.Date;

@Data
public class ListReviewResponseWithoutFields {


    private Long id;
    private String listName;
    private Date creationDate;
    private Date lastUpdate;
    private UserResponse owner;


    public ListReviewResponseWithoutFields(ListReview list){
        this.owner = new UserResponse(list.getOwner());
        this.id = list.getId();
        this.listName = list.getListName();
        this.creationDate = list.getCreationDate();
        this.lastUpdate = list.getLastUpdate();
    }

}
