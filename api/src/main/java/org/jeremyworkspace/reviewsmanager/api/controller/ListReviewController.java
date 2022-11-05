package org.jeremyworkspace.reviewsmanager.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jeremyworkspace.reviewsmanager.api.model.*;
import org.jeremyworkspace.reviewsmanager.api.model.helper.FieldType;
import org.jeremyworkspace.reviewsmanager.api.model.response.FieldResponse;
import org.jeremyworkspace.reviewsmanager.api.model.response.ListReviewResponse;
import org.jeremyworkspace.reviewsmanager.api.model.response.UserResponse;
import org.jeremyworkspace.reviewsmanager.api.service.FieldService;
import org.jeremyworkspace.reviewsmanager.api.service.ListReviewService;
import org.jeremyworkspace.reviewsmanager.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.*;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/api/lists")
public class ListReviewController {

    @Autowired
    private ListReviewService listReviewService;

    @Autowired
    private FieldService fieldService;

    @GetMapping("{id}")
    public ResponseEntity<ListReviewResponse> getListById(@PathVariable("id") final Long id, @AuthenticationPrincipal User user){
        // TODO : Factoriser le code de vérification d'appartenance à un user.
        ListReviewResponse list = new ListReviewResponse(this.listReviewService.getListById(id).orElseThrow());
        if(user.getId() != list.getOwner().getId()){
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity deleteList(@PathVariable("id") final Long id, @AuthenticationPrincipal User user){
        ListReview l = this.listReviewService.getListById(id).orElseThrow();
        if(user.getId() != l.getOwner().getId()){
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }

        this.listReviewService.deleteListById(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("{id}/fields")
    public ResponseEntity<FieldResponse> createField(HttpServletRequest request, @PathVariable("id") final Long id, @RequestParam String type) throws IOException, FormatException {

        // TODO Ajouter contrôle que la list appartient à l'user authentifié.

        // TODO : XSS prevent sur le nom du champ

        // Trying to retrieve the list.
        ListReview listReview = this.listReviewService.getListById(id).orElseThrow();

        // Depending on the type, retrieving the correct Field object.
        ObjectMapper objectMapper = new ObjectMapper();
        Field field = null;

        try {
            type = type.toUpperCase();

            if(FieldType.SCORE.name().equals(type))
                field = objectMapper.readValue(request.getInputStream(), ScoreField.class);
            if(FieldType.DATE.name().equals(type))
                field = objectMapper.readValue(request.getInputStream(), DateField.class);
            if(FieldType.TEXT.name().equals(type))
                field = objectMapper.readValue(request.getInputStream(), TextField.class);

        } catch (IOException e) {
            throw new FormatException("Wrong format", e);
        }

        if(field == null){
            throw new FormatException("Wrong type of field", null);
        }

        field.setListReview(listReview);

        FieldResponse fieldResponse = new FieldResponse(this.fieldService.saveField(field));
        return new ResponseEntity<FieldResponse>(fieldResponse, HttpStatus.CREATED);
    }

}