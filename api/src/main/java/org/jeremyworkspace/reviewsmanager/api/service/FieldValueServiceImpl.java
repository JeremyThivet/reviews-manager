package org.jeremyworkspace.reviewsmanager.api.service;

import org.jeremyworkspace.reviewsmanager.api.model.FieldValue;
import org.jeremyworkspace.reviewsmanager.api.repository.FieldValueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class FieldValueServiceImpl implements FieldValueService{

    @Autowired
    private FieldValueRepository fieldValueRepository;

    @Override
    public FieldValue saveFieldValue(FieldValue field) {
        return this.fieldValueRepository.save(field);
    }

    @Override
    public void deleteFieldValueById(Long id) {
        this.fieldValueRepository.deleteById(id);
    }

    @Override
    public Optional<FieldValue> getFieldValueById(Long id) {
        return this.fieldValueRepository.findById(id);
    }
}
