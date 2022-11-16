package org.jeremyworkspace.reviewsmanager.api.service;

import org.jeremyworkspace.reviewsmanager.api.controller.exception.FormatException;
import org.jeremyworkspace.reviewsmanager.api.controller.exception.UpdateEntryException;
import org.jeremyworkspace.reviewsmanager.api.controller.exception.WrongOwnerException;
import org.jeremyworkspace.reviewsmanager.api.model.*;
import org.jeremyworkspace.reviewsmanager.api.model.dto.EntryDto;
import org.jeremyworkspace.reviewsmanager.api.model.helper.FieldType;
import org.jeremyworkspace.reviewsmanager.api.repository.EntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class EntryServiceImpl implements EntryService{

    @Autowired
    private FieldService fieldService;

    @Autowired
    private FieldValueService fieldValueService;

    @Autowired
    private EntryRepository entryRepository;


    @Override
    public Entry saveEntry(Entry entry) {
        return this.entryRepository.save(entry);
    }

    @Override
    public Entry createEntryFromEntryDto(EntryDto entryDto, ListReview listOwner, User owner) throws WrongOwnerException, FormatException {

        // Retrieving all fields, creating a list of fieldvalue
        List<FieldValue> fieldValues = new LinkedList<>();
        Map<Long, String> fieldWithValues = entryDto.getFieldsWithValues();

        // Iterating over field with values
        for(Long idField : fieldWithValues.keySet()) {
            Field field = fieldService.getFieldById(idField).orElseThrow();

            // Check if the field belongs to the list we will add the entry to.
            if(field.getListReview().getId() != listOwner.getId()){
                throw new WrongOwnerException("Le champ ciblé n'appartient pas à la liste ciblée.");
            }

            String value = fieldWithValues.get(idField);
            FieldValue fieldValueToAdd = FieldValueFactory.getFieldValue(field, value, false);

            fieldValues.add(fieldValueToAdd);
        }

        // Adding entry
        Entry entry = new Entry();
        entry.setEntryName(entryDto.getEntryName());
        entry.setCreationDate(new Date());
        entry.setLastUpdate(new Date());
        entry.setCreator(owner);
        entry.setListReview(listOwner);

        // Adding FieldValues
        for(FieldValue fv : fieldValues){
            fv.setEntry(entry);
        }
        entry.setFieldValues(fieldValues);

        return this.saveEntry(entry);
    }

    @Override
    public Entry updateEntryFromEntryDto(EntryDto entryDto, Entry entryToUpdate, User owner) throws WrongOwnerException, FormatException, UpdateEntryException {

        if(entryToUpdate.getFieldValues() == null || entryToUpdate.getFieldValues().isEmpty()){
            throw new UpdateEntryException("L'entrée ne possède aucun champ de valeur.");
        }

        // Retrieving all fields, updating every fieldvalues
        Map<Long, String> fieldWithValues = entryDto.getFieldsWithValues();

        // Iterating over field with values
        for(Long idField : fieldWithValues.keySet()) {
            try {
                String value = fieldWithValues.get(idField);
                List<FieldValue> fieldValueList = entryToUpdate.getFieldValues();
                FieldValue fieldValue = entryToUpdate.getFieldValues().stream().filter(fv -> fv.getField().getId() == idField).findFirst().orElseThrow();

                fieldValue.setValueFromString(value);

            } catch (NoSuchElementException e){
                throw new UpdateEntryException("Le champ " + idField + " ne fait pas partie de l'entrée ciblée.");
            }
        }

        entryToUpdate.setLastUpdate(new Date());
        entryToUpdate.setEntryName(entryDto.getEntryName());
        return this.saveEntry(entryToUpdate);
    }

    @Override
    public Entry addDefaultFieldToEntry(Entry entry, Field field) throws FormatException {

        if(entry.getFieldValues() == null || entry.getFieldValues().isEmpty()){
            throw new RuntimeException("A default value for a field must not been added to an empty list.");
        }

        FieldValue fieldValueToAdd = FieldValueFactory.getFieldValue(field, "", true);
        fieldValueToAdd.setEntry(entry);

        FieldValue fieldValueAdded = this.fieldValueService.saveFieldValue(fieldValueToAdd);
        entry.getFieldValues().add(fieldValueAdded);

        return entry;
    }

    @Override
    public void deleteEntryById(Long id) {
        this.entryRepository.deleteById(id);
    }

    @Override
    public Optional<Entry> getEntryById(Long id) {
        return this.entryRepository.findById(id);
    }
}
