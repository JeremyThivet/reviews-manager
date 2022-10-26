package org.jeremyworkspace.reviewsmanager.api.repository.fieldfactory.handler;

import org.jeremyworkspace.reviewsmanager.api.model.helper.FieldType;
import org.jeremyworkspace.reviewsmanager.api.repository.fieldfactory.DateFieldRepository;
import org.jeremyworkspace.reviewsmanager.api.repository.fieldfactory.FieldRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DateFieldHandler implements FieldHandler{

    @Autowired
    private DateFieldRepository dateFieldRepository;

    @Override
    public boolean canHandle(FieldType fieldType) {
        return fieldType == FieldType.DATE;
    }

    @Override
    public FieldRepository getRepository() {
        return dateFieldRepository;
    }
}
