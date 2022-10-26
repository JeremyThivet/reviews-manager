package org.jeremyworkspace.reviewsmanager.api.service;

import org.jeremyworkspace.reviewsmanager.api.model.Field;
import org.jeremyworkspace.reviewsmanager.api.model.TextField;

import java.util.Optional;

public interface FieldService {

    Field saveField(Field field);

    void deleteFieldById(Long id);

    Optional<Field> getFieldById(Long id);
}
