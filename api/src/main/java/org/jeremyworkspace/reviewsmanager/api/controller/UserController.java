package org.jeremyworkspace.reviewsmanager.api.controller;

import org.jeremyworkspace.reviewsmanager.api.model.ListReview;
import org.jeremyworkspace.reviewsmanager.api.model.User;
import org.jeremyworkspace.reviewsmanager.api.model.dto.UserDto;
import org.jeremyworkspace.reviewsmanager.api.model.response.ListReviewResponse;
import org.jeremyworkspace.reviewsmanager.api.model.response.UserResponse;
import org.jeremyworkspace.reviewsmanager.api.service.ListReviewService;
import org.jeremyworkspace.reviewsmanager.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.*;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

// Indique à Spring d'insérer le retour de la méthode au format JSON dans la requête HTTP.
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ListReviewService listReviewService;

    @GetMapping("{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable("id") final Long id){
        UserResponse user = new UserResponse(this.userService.getUserById(id).orElseThrow());
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Iterable<UserResponse>> getUsers() {
        Iterable<User> users = this.userService.getUsers();
        List<UserResponse> usersResponse = new LinkedList<UserResponse>();

        users.forEach((user) -> {
            UserResponse toAdd = new UserResponse(user);
            usersResponse.add(toAdd);
        });

        return new ResponseEntity<>(usersResponse, HttpStatus.OK);
    }

    @GetMapping("/count")
    public ResponseEntity<Long> getUsersCount() {
        return new ResponseEntity<>(this.userService.countUsers(), HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public void deleteUser(@PathVariable("id") final Long id){
        this.userService.deleteUserById(id);
    }

    /**
     * This methods handle a POST to /users and try to create a user using the UserService.
     * @param user
     * @return user created, without the password hash.
     */
    @PostMapping()
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserDto user){
        User u = new User(user);

        return new ResponseEntity<>(this.saveUser(u), HttpStatus.CREATED);
    }

    @PostMapping("{id}/lists")
    public ResponseEntity<ListReviewResponse> createList(@PathVariable("id") final Long userId, @Valid @RequestBody ListReview listReview){
        User u = this.userService.getUserById(userId).orElseThrow();
        listReview.setOwner(u);
        return new ResponseEntity<>(this.saveList(listReview), HttpStatus.CREATED);
    }

    @GetMapping("{id}/lists")
    public ResponseEntity<Iterable<ListReviewResponse>> getUserLists(@PathVariable("id") final Long userId){
        Iterable<ListReviewResponse> result = StreamSupport.stream(this.listReviewService.findListsByUserId(userId).spliterator(), false)
                .map((listReview) -> new ListReviewResponse(listReview)).collect(Collectors.toList());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    private UserResponse saveUser(User user){
        User u = this.userService.saveUser(user);
        return new UserResponse(u);
    }

    private ListReviewResponse saveList(ListReview list){
        ListReview l = this.listReviewService.saveList(list);
        return new ListReviewResponse(list);
    }



}