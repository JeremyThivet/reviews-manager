package org.jeremyworkspace.reviewsmanager.api.repository.fieldfactory.handler;

import org.jeremyworkspace.reviewsmanager.api.model.helper.FieldType;
import org.jeremyworkspace.reviewsmanager.api.repository.fieldfactory.FieldRepository;
import org.jeremyworkspace.reviewsmanager.api.repository.fieldfactory.TextFieldRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TextFieldHandler implements FieldHandler{

    @Autowired
    private TextFieldRepository textFieldRepository;

    @Override
    public boolean canHandle(FieldType fieldType) {
        return fieldType == FieldType.TEXT;
    }

    @Override
    public FieldRepository getRepository() {
        return textFieldRepository;
    }
}
