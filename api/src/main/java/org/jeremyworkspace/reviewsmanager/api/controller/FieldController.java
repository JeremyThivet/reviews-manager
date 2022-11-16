package org.jeremyworkspace.reviewsmanager.api.controller;

import org.jeremyworkspace.reviewsmanager.api.controller.exception.WrongOwnerException;
import org.jeremyworkspace.reviewsmanager.api.model.*;
import org.jeremyworkspace.reviewsmanager.api.model.helper.OwnershipVerifier;
import org.jeremyworkspace.reviewsmanager.api.service.FieldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/fields")
public class FieldController {

    @Autowired
    private FieldService fieldService;

    @Autowired
    private OwnershipVerifier ownershipVerifier;

    @DeleteMapping("{id}")
    public ResponseEntity deleteList(@PathVariable("id") final Long id, @AuthenticationPrincipal User user) throws WrongOwnerException {

        Field f = this.fieldService.getFieldById(id).orElseThrow();
        this.ownershipVerifier.doesListBelongsToUser(f.getListReview(), user);

        this.fieldService.deleteFieldById(id);
        return ResponseEntity.ok().build();
    }

}