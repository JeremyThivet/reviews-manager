package org.jeremyworkspace.reviewsmanager.api.repository.fieldfactory;

import org.jeremyworkspace.reviewsmanager.api.model.helper.FieldType;
import org.jeremyworkspace.reviewsmanager.api.repository.fieldfactory.handler.FieldHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class FieldRepositoryFactory {

    @Autowired
    private List<FieldHandler> fieldHandlers;


    public FieldRepository getFieldFactory(FieldType type) {

        return fieldHandlers.stream()
                .filter(fieldRepository -> fieldRepository.canHandle(type))
                .findFirst().orElseThrow().getRepository();
    }

}
