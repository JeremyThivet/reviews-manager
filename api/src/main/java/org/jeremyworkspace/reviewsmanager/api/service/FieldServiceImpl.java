package org.jeremyworkspace.reviewsmanager.api.service;

import org.jeremyworkspace.reviewsmanager.api.model.Field;
import org.jeremyworkspace.reviewsmanager.api.repository.FieldRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FieldServiceImpl implements FieldService{

    @Autowired
    private FieldRepository fieldRepository;

    @Override
    public Field saveField(Field field) {
        return this.fieldRepository.save(field);
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
