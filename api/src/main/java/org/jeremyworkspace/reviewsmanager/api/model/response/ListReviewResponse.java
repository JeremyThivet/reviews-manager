package org.jeremyworkspace.reviewsmanager.api.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.jeremyworkspace.reviewsmanager.api.controller.exception.FormatException;
import org.jeremyworkspace.reviewsmanager.api.model.*;
import org.jeremyworkspace.reviewsmanager.api.model.helper.FieldType;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

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

    private List<FieldResponseWithoutList> fields;

    private List<EntryResponse> entries;

    public ListReviewResponse(ListReview list){
        this.owner = new UserResponse(list.getOwner());
        this.id = list.getId();
        this.listName = list.getListName();
        this.creationDate = list.getCreationDate();
        this.lastUpdate = list.getLastUpdate();
        this.fields = !(list.getFields() == null || list.getFields().isEmpty()) ? list.getFields().stream().map(f -> {
                    try {
                        return FieldResponseWithoutListFactory.getFieldResponseWithoutList(f);
                    } catch (FormatException e) {
                        throw new RuntimeException(e);
                    }
                }
        ).collect(Collectors.toList()) : new ArrayList<>();

        this.entries = !(list.getEntries() == null || list.getEntries().isEmpty()) ?
                        list.getEntries().stream().map(e -> new EntryResponse(e)).collect(Collectors.toList())
                        :
                        new ArrayList<>();
    }
}
