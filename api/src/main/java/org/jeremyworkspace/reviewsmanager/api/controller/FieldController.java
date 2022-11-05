package org.jeremyworkspace.reviewsmanager.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jeremyworkspace.reviewsmanager.api.model.*;
import org.jeremyworkspace.reviewsmanager.api.model.helper.FieldType;
import org.jeremyworkspace.reviewsmanager.api.model.response.FieldResponse;
import org.jeremyworkspace.reviewsmanager.api.model.response.ListReviewResponse;
import org.jeremyworkspace.reviewsmanager.api.model.response.UserResponse;
import org.jeremyworkspace.reviewsmanager.api.service.FieldService;
import org.jeremyworkspace.reviewsmanager.api.service.FieldServiceImpl;
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
@RequestMapping("/api/fields")
public class FieldController {

    @Autowired
    private FieldService fieldService;

    @DeleteMapping("{id}")
    public ResponseEntity deleteList(@PathVariable("id") final Long id, @AuthenticationPrincipal User user){
        Field f = this.fieldService.getFieldById(id).orElseThrow();
        if(user.getId() != f.getListReview().getOwner().getId()){
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }

        this.fieldService.deleteFieldById(id);
        return ResponseEntity.ok().build();
    }

}