package org.jeremyworkspace.reviewsmanager.api.repository.fieldfactory;

import org.jeremyworkspace.reviewsmanager.api.model.TextField;
import org.jeremyworkspace.reviewsmanager.api.model.helper.FieldType;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Repository
public interface TextFieldRepository extends FieldRepository<TextField> {
}
