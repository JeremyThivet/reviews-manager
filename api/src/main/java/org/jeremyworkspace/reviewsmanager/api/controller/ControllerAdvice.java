package org.jeremyworkspace.reviewsmanager.api.controller;

import org.hibernate.exception.ConstraintViolationException;
import org.jeremyworkspace.reviewsmanager.api.controller.exception.FormatException;
import org.jeremyworkspace.reviewsmanager.api.controller.exception.WrongOwnerException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class ControllerAdvice
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


    @ExceptionHandler({NoSuchElementException.class, EmptyResultDataAccessException.class})
    public ResponseEntity<Map<String, String>> handleNoSuchElementsExceptions() {
        HashMap<String, String> response = new HashMap<String,String>();
        response.put("error", "Aucun objet trouvé pour cet id");
        return new ResponseEntity<Map<String, String>>(response, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(FormatException.class)
    public ResponseEntity<Map<String, String>> handleFormatException(
            FormatException ex) {
        HashMap<String, String> response = new HashMap<String,String>();
        response.put("error", ex.getMessage());
        return new ResponseEntity<Map<String, String>>(response, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(WrongOwnerException.class)
    public ResponseEntity<Map<String, String>> handleWrongOwnerException(
            WrongOwnerException ex) {
        HashMap<String, String> response = new HashMap<String,String>();
        response.put("error", ex.getMessage());
        return new ResponseEntity<Map<String, String>>(response, HttpStatus.BAD_REQUEST);
    }

}