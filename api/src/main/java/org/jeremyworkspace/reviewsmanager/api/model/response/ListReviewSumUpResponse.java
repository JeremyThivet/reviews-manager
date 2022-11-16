package org.jeremyworkspace.reviewsmanager.api.model.response;

import lombok.Data;
import org.jeremyworkspace.reviewsmanager.api.model.DateField;
import org.jeremyworkspace.reviewsmanager.api.model.ListReview;
import org.jeremyworkspace.reviewsmanager.api.model.ScoreField;
import org.jeremyworkspace.reviewsmanager.api.model.TextField;
import org.jeremyworkspace.reviewsmanager.api.model.helper.FieldType;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class ListReviewSumUpResponse {

    private Long id;
    private String listName;
    private Date creationDate;
    private Date lastUpdate;
    private UserResponse owner;
    private List<FieldResponseWithoutList> fields;

    private int numberOfEntries;

    public ListReviewSumUpResponse(ListReview list){
        this.owner = new UserResponse(list.getOwner());
        this.id = list.getId();
        this.listName = list.getListName();
        this.creationDate = list.getCreationDate();
        this.lastUpdate = list.getLastUpdate();
        this.fields = !(list.getFields() == null || list.getFields().isEmpty()) ? list.getFields().stream().map(f -> {
                    if(f.getType() == FieldType.SCORE)
                        return new ScoreFieldResponseWithoutList((ScoreField) f);
                    if(f.getType() == FieldType.DATE)
                        return new DateFieldResponseWithoutList((DateField)f);
                    if(f.getType() == FieldType.TEXT)
                        return new TextFieldResponseWithoutList((TextField)f);

                    return null;
                }
        ).collect(Collectors.toList()) : new ArrayList<>();

        this.numberOfEntries = list.getEntries().size();
    }
}
