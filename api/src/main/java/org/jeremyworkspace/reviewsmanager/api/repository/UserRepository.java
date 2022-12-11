package org.jeremyworkspace.reviewsmanager.api.repository;

import org.jeremyworkspace.reviewsmanager.api.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findFirstByUsernameIgnoreCase(String username);

}
