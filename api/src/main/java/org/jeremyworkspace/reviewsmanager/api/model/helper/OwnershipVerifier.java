package org.jeremyworkspace.reviewsmanager.api.model.helper;

import org.jeremyworkspace.reviewsmanager.api.controller.exception.WrongOwnerException;
import org.jeremyworkspace.reviewsmanager.api.model.Entry;
import org.jeremyworkspace.reviewsmanager.api.model.ListReview;
import org.jeremyworkspace.reviewsmanager.api.model.User;

/**
 * This class will check if ownership of an object is valid (e.g. a list belongs to a specified user)
 */
public interface OwnershipVerifier {

    public boolean doesListBelongsToUser(ListReview list, User user) throws WrongOwnerException;

    public boolean doesEntryBelongsToUser(Entry entry, User user) throws WrongOwnerException;

    public boolean doesUserHasId(User u, Long id) throws WrongOwnerException;

}
