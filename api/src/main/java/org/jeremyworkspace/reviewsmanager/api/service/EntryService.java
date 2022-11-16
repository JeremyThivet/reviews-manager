package org.jeremyworkspace.reviewsmanager.api.service;

import org.jeremyworkspace.reviewsmanager.api.controller.exception.FormatException;
import org.jeremyworkspace.reviewsmanager.api.controller.exception.WrongOwnerException;
import org.jeremyworkspace.reviewsmanager.api.model.Entry;
import org.jeremyworkspace.reviewsmanager.api.model.Field;
import org.jeremyworkspace.reviewsmanager.api.model.ListReview;
import org.jeremyworkspace.reviewsmanager.api.model.User;
import org.jeremyworkspace.reviewsmanager.api.model.dto.EntryDto;

import java.util.Optional;

public interface EntryService {

    Entry saveEntry(Entry entry);

    Entry saveEntryFromEntryDto(EntryDto entryDto, ListReview listOwner, User owner) throws WrongOwnerException, FormatException;

    /**
     * This methods add a default fieldvalue of field to the entry (if a field is added although there already are entries in the list)
     * @param entry Entry to update
     * @param field The field that has been just added to the list.
     * @return The entry containing the new fieldvalue
     */
    Entry addDefaultFieldToEntry(Entry entry, Field field) throws FormatException;

    void deleteEntryById(Long id);

    Optional<Entry> getEntryById(Long id);
}
