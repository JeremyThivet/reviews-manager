package org.jeremyworkspace.reviewsmanager.api.controller;

import org.jeremyworkspace.reviewsmanager.api.controller.exception.FormatException;
import org.jeremyworkspace.reviewsmanager.api.controller.exception.UpdateEntryException;
import org.jeremyworkspace.reviewsmanager.api.controller.exception.WrongOwnerException;
import org.jeremyworkspace.reviewsmanager.api.model.Entry;
import org.jeremyworkspace.reviewsmanager.api.model.User;
import org.jeremyworkspace.reviewsmanager.api.model.dto.EntryDto;
import org.jeremyworkspace.reviewsmanager.api.model.helper.OwnershipVerifier;
import org.jeremyworkspace.reviewsmanager.api.model.response.EntryResponse;
import org.jeremyworkspace.reviewsmanager.api.service.EntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

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

    @PutMapping("{id}")
    public ResponseEntity<EntryResponse> updateEntry(@RequestBody EntryDto entryDto, @PathVariable("id") final Long id, @AuthenticationPrincipal User user) throws IOException, FormatException, WrongOwnerException, UpdateEntryException {

        // Trying to retrieve the list.
        Entry entry = this.entryService.getEntryById(id).orElseThrow();
        this.ownershipVerifier.doesEntryBelongsToUser(entry, user);

        EntryResponse entryResponse = new EntryResponse(this.entryService.updateEntryFromEntryDto(entryDto, entry, user));

        return new ResponseEntity<EntryResponse>(entryResponse, HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity deleteEntry(@PathVariable("id") final Long id, @AuthenticationPrincipal User user) throws WrongOwnerException {

        Entry e = this.entryService.getEntryById(id).orElseThrow();
        this.ownershipVerifier.doesEntryBelongsToUser(e, user);

        this.entryService.deleteEntryById(id);
        return ResponseEntity.ok().build();
    }

}