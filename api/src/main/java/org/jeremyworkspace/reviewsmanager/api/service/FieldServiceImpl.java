package org.jeremyworkspace.reviewsmanager.api.service;

import org.jeremyworkspace.reviewsmanager.api.model.Field;
import org.jeremyworkspace.reviewsmanager.api.repository.fieldfactory.FieldRepository;
import org.jeremyworkspace.reviewsmanager.api.repository.fieldfactory.FieldRepositoryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FieldServiceImpl implements FieldService{

    @Autowired
    private FieldRepositoryFactory fieldRepositoryFactory;

    @Autowired
    private FieldRepository fieldRepository;

    @Override
    public Field saveField(Field field) {
        //return (Field) fieldRepositoryFactory.getFieldFactory(field.getType()).save(field);
        return (Field) this.fieldRepository.save(field);
    }

    @Override
    public void deleteFieldById(Long id) {
        this.fieldRepository.deleteById(id);
    }

    @Override
    public Optional<Field> getFieldById(Long id) {
        return fieldRepository.findById(id);
    }
}
