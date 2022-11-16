package org.jeremyworkspace.reviewsmanager.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jeremyworkspace.reviewsmanager.api.controller.exception.FormatException;
import org.jeremyworkspace.reviewsmanager.api.controller.exception.WrongOwnerException;
import org.jeremyworkspace.reviewsmanager.api.model.*;
import org.jeremyworkspace.reviewsmanager.api.model.dto.EntryDto;
import org.jeremyworkspace.reviewsmanager.api.model.helper.FieldType;
import org.jeremyworkspace.reviewsmanager.api.model.helper.OwnershipVerifier;
import org.jeremyworkspace.reviewsmanager.api.model.response.*;
import org.jeremyworkspace.reviewsmanager.api.service.EntryService;
import org.jeremyworkspace.reviewsmanager.api.service.FieldService;
import org.jeremyworkspace.reviewsmanager.api.service.ListReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/lists")
public class ListReviewController {

    @Autowired
    private ListReviewService listReviewService;

    @Autowired
    private FieldService fieldService;

    @Autowired
    private EntryService entryService;

    @Autowired
    private OwnershipVerifier ownershipVerifier;

    @GetMapping("{id}")
    public ResponseEntity<ListReviewResponse> getListById(@PathVariable("id") final Long id, @AuthenticationPrincipal User user) throws WrongOwnerException {

        ListReview listReview = this.listReviewService.getListById(id).orElseThrow();
        this.ownershipVerifier.doesListBelongsToUser(listReview, user);

        ListReviewResponse listReviewResponse = new ListReviewResponse(listReview);
        return new ResponseEntity<>(listReviewResponse, HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity deleteList(@PathVariable("id") final Long id, @AuthenticationPrincipal User user) throws WrongOwnerException {

        ListReview listReview = this.listReviewService.getListById(id).orElseThrow();
        this.ownershipVerifier.doesListBelongsToUser(listReview, user);

        this.listReviewService.deleteListById(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("{id}/fields")
    public ResponseEntity<FieldResponseWithoutList> createField(HttpServletRequest request, @PathVariable("id") final Long id, @RequestParam String type, @AuthenticationPrincipal User user) throws IOException, FormatException, WrongOwnerException {

        // Trying to retrieve the list.
        ListReview listReview = this.listReviewService.getListById(id).orElseThrow();
        this.ownershipVerifier.doesListBelongsToUser(listReview, user);

        // Depending on the type, retrieving the correct Field object.
        ObjectMapper objectMapper = new ObjectMapper();
        Field field = null;

        try {
            Class<? extends Field> classType = FieldFactory.getClassFromType(FieldType.valueOf(type.toUpperCase()));
            field = objectMapper.readValue(request.getInputStream(), classType);

        } catch (IOException e) {
            throw new FormatException("Wrong format in body request", e);
        }

        field.setListReview(listReview);
        Field fieldSaved = this.fieldService.saveField(field);

        // If lists already has entries, adding a default value to that new field.
        List<Entry> entries = listReview.getEntries();
        if(entries != null && !entries.isEmpty()){
            for(Entry e : entries){
                this.entryService.addDefaultFieldToEntry(e, fieldSaved);
            }
        }

        FieldResponseWithoutList fieldResponseWithoutList = FieldResponseWithoutListFactory.getFieldResponseWithoutList(fieldSaved);
        return new ResponseEntity<FieldResponseWithoutList>(fieldResponseWithoutList, HttpStatus.CREATED);
    }


    @PostMapping("{id}/entries")
    public ResponseEntity<EntryResponse> createEntry(@RequestBody EntryDto entryDto, @PathVariable("id") final Long id, @AuthenticationPrincipal User user) throws IOException, FormatException, WrongOwnerException {

        // Trying to retrieve the list.
        ListReview listReview = this.listReviewService.getListById(id).orElseThrow();
        this.ownershipVerifier.doesListBelongsToUser(listReview, user);

        EntryResponse entry = new EntryResponse(this.entryService.createEntryFromEntryDto(entryDto, listReview, user));

        return new ResponseEntity<EntryResponse>(entry, HttpStatus.CREATED);
    }

}