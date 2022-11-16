package org.jeremyworkspace.reviewsmanager.api.model.dto;

import lombok.Data;

import javax.persistence.Column;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Map;

/**
 * This class stands for an Entry Dto. It corresponds to an Entry as it must be passed in the body request in order to create a new entry.
 * When you create your entry, you should want to associate some Field Values and this is the class which will retrieve these datas.
 */
@Data
public class EntryDto {

    @NotEmpty(message = "{validation.entry.notEmpty}")
    @Size(min = 1, max = 100, message = "{validation.entry.size}")
    private String entryName;

    private Map<Long, String> fieldsWithValues;

}
