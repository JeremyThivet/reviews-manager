package org.jeremyworkspace.reviewsmanager.api.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.jeremyworkspace.reviewsmanager.api.controller.exception.FormatException;
import org.jeremyworkspace.reviewsmanager.api.model.*;
import org.jeremyworkspace.reviewsmanager.api.model.helper.FieldType;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class EntryResponse {

    private Long id;
    private String entryName;
    private Date creationDate;
    private Date lastUpdate;
    private UserResponse creator;
    private List<FieldValueResponse> fieldValueList;

    public EntryResponse(Entry entry){
        this.id = entry.getId();
        this.entryName = entry.getEntryName();
        this.creationDate = entry.getCreationDate();
        this.lastUpdate = entry.getLastUpdate();
        this.creator = new UserResponse(entry.getCreator());

        this.fieldValueList = !(entry.getFieldValues() == null || entry.getFieldValues().isEmpty()) ? entry.getFieldValues().stream().map(fieldValue -> {
                    try {
                        return FieldValueResponseFactory.getFieldValueResponse(fieldValue);
                    } catch (FormatException e) {
                        throw new RuntimeException(e);
                    }
                }
        ).collect(Collectors.toList()) : new ArrayList<>();
    }


}
