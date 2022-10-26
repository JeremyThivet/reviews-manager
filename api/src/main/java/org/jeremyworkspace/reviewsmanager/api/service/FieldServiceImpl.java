package org.jeremyworkspace.reviewsmanager.api.service;

import org.jeremyworkspace.reviewsmanager.api.model.Field;
import org.jeremyworkspace.reviewsmanager.api.repository.fieldfactory.FieldRepositoryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FieldServiceImpl implements FieldService{

    @Autowired
    private FieldRepositoryFactory fieldRepositoryFactory;

    @Override
    public Field saveField(Field field) {
        return (Field) fieldRepositoryFactory.getFieldFactory(field.getType()).save(field);
    }

    @Override
    public void deleteFieldById(Long id) {

    }

    @Override
    public Optional<Field> getFieldById(Long id) {
        return Optional.empty();
    }
}
