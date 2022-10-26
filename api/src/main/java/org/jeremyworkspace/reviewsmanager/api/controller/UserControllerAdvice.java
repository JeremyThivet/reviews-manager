package org.jeremyworkspace.reviewsmanager.api.controller;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.PersistenceException;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class UserControllerAdvice
{
    @ExceptionHandler(DataIntegrityViolationException.class)
    public void handleException(Exception ex, HttpServletResponse response) {
        response.setStatus(400);
        ConstraintViolationException innerEx = (ConstraintViolationException) ex.getCause();
        String sqlState = innerEx.getSQLException().getSQLState();

        // If it concerns a UNIQUE constraint, then the return code is 460.
        if(sqlState.equals("23505")) {
            response.setStatus(460);
        }
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NoSuchElementException.class)
    public String handleNoSuchElementsExceptions(
            NoSuchElementException ex) {
        // TODO : Essayer d'avoir un message par type d'objet.
        return "Aucun utilisateur trouvé pour cet id.";
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(EmptyResultDataAccessException.class)
    public String handleEmptyResultDataAccessException(
            EmptyResultDataAccessException ex) {
        return "Aucun utilisateur trouvé pour cet id.";
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(FormatException.class)
    public String handleFormatException(
            FormatException ex) {
        return ex.getMessage();
    }

}