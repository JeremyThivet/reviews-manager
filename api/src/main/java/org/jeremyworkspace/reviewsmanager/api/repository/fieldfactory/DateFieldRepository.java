package org.jeremyworkspace.reviewsmanager.api.repository.fieldfactory;

import org.jeremyworkspace.reviewsmanager.api.model.DateField;
import org.jeremyworkspace.reviewsmanager.api.model.helper.FieldType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Repository
public interface DateFieldRepository extends FieldRepository<DateField> {
}
