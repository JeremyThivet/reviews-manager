package org.jeremyworkspace.reviewsmanager.api.repository.fieldfactory;

import org.jeremyworkspace.reviewsmanager.api.model.Field;
import org.springframework.data.repository.CrudRepository;

public interface FieldRepository<T extends Field> extends CrudRepository<T, Long> {

}
