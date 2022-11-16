package org.jeremyworkspace.reviewsmanager.api.controller;

import org.jeremyworkspace.reviewsmanager.api.controller.exception.WrongOwnerException;
import org.jeremyworkspace.reviewsmanager.api.model.Entry;
import org.jeremyworkspace.reviewsmanager.api.model.Field;
import org.jeremyworkspace.reviewsmanager.api.model.User;
import org.jeremyworkspace.reviewsmanager.api.model.helper.OwnershipVerifier;
import org.jeremyworkspace.reviewsmanager.api.model.response.EntryResponse;
import org.jeremyworkspace.reviewsmanager.api.model.response.ListReviewResponse;
import org.jeremyworkspace.reviewsmanager.api.service.EntryService;
import org.jeremyworkspace.reviewsmanager.api.service.FieldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/entries")
public class EntryController {

    @Autowired
    private EntryService entryService;

    @Autowired
    private OwnershipVerifier ownershipVerifier;

    @GetMapping("{id}")
    public ResponseEntity<EntryResponse> getEntryById(@PathVariable("id") final Long id, @AuthenticationPrincipal User user){
        EntryResponse entry = new EntryResponse(this.entryService.getEntryById(id).orElseThrow());
        return new ResponseEntity<EntryResponse>(entry, HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity deleteList(@PathVariable("id") final Long id, @AuthenticationPrincipal User user) throws WrongOwnerException {

        Entry e = this.entryService.getEntryById(id).orElseThrow();
        this.ownershipVerifier.doesEntryBelongsToUser(e, user);

        this.entryService.deleteEntryById(id);
        return ResponseEntity.ok().build();
    }

}