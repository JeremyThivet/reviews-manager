package org.jeremyworkspace.reviewsmanager.api.service;

import org.jeremyworkspace.reviewsmanager.api.model.User;

import java.util.Optional;

public interface UserService {
    Optional<User> getUserById(Long id);

    Iterable<User> getUsers();

    User createUser(User user);

    User updateUser(User user);

    void deleteUserById(Long id);

    long countUsers();

    Optional<User> getUserByUsername(String username);

}
