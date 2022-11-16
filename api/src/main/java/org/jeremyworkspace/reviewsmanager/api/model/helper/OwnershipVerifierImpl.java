package org.jeremyworkspace.reviewsmanager.api.model.helper;

import org.jeremyworkspace.reviewsmanager.api.controller.exception.WrongOwnerException;
import org.jeremyworkspace.reviewsmanager.api.model.Entry;
import org.jeremyworkspace.reviewsmanager.api.model.ListReview;
import org.jeremyworkspace.reviewsmanager.api.model.User;
import org.springframework.stereotype.Component;

@Component
public class OwnershipVerifierImpl implements OwnershipVerifier {

    @Override
    public boolean doesListBelongsToUser(ListReview list, User user) throws WrongOwnerException {
        if(user.getId() != list.getOwner().getId()){
            throw new WrongOwnerException("La liste ciblée n'appartient pas à l'utilisateur connecté.");
        }
        return true;
    }

    @Override
    public boolean doesEntryBelongsToUser(Entry entry, User user) throws WrongOwnerException {
        if(entry.getCreator().getId() != user.getId()){
            throw new WrongOwnerException("L'entrée' ciblée n'appartient pas à l'utilisateur connecté.");
        }
        return true;
    }
}
