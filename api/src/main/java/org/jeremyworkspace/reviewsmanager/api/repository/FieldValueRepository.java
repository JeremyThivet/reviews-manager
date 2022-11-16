package org.jeremyworkspace.reviewsmanager.api.repository;

import org.jeremyworkspace.reviewsmanager.api.model.Field;
import org.jeremyworkspace.reviewsmanager.api.model.FieldValue;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FieldValueRepository extends CrudRepository<FieldValue, Long> {

}