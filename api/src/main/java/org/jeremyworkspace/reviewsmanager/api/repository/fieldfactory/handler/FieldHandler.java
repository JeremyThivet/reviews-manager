package org.jeremyworkspace.reviewsmanager.api.repository.fieldfactory.handler;

import org.jeremyworkspace.reviewsmanager.api.model.helper.FieldType;
import org.jeremyworkspace.reviewsmanager.api.repository.fieldfactory.FieldRepository;

public interface FieldHandler {
    public boolean canHandle(FieldType fieldType);

    public FieldRepository getRepository();
}
