package org.jeremyworkspace.reviewsmanager.api.controller;

import org.jeremyworkspace.reviewsmanager.api.model.ListReview;
import org.jeremyworkspace.reviewsmanager.api.model.User;
import org.jeremyworkspace.reviewsmanager.api.model.response.ListReviewResponse;
import org.jeremyworkspace.reviewsmanager.api.model.response.UserResponse;
import org.jeremyworkspace.reviewsmanager.api.service.ListReviewService;
import org.jeremyworkspace.reviewsmanager.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.*;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/api/lists")
public class ListReviewController {

    @Autowired
    private ListReviewService listReviewService;

    @GetMapping("{id}")
    public ResponseEntity<ListReviewResponse> getListById(@PathVariable("id") final Long id){
        ListReviewResponse list = new ListReviewResponse(this.listReviewService.getListById(id).orElseThrow());
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

}