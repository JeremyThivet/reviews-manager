package org.jeremyworkspace.reviewsmanager.api.repository;

import org.jeremyworkspace.reviewsmanager.api.model.Field;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FieldRepository extends CrudRepository<Field, Long> {

}
