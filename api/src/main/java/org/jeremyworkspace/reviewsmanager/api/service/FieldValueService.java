package org.jeremyworkspace.reviewsmanager.api.service;

import org.jeremyworkspace.reviewsmanager.api.model.FieldValue;

import java.util.Optional;

public interface FieldValueService {

    FieldValue saveFieldValue(FieldValue field);

    void deleteFieldValueById(Long id);

    Optional<FieldValue> getFieldValueById(Long id);
}
