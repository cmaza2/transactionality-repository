package com.maza.peoplemanagementservice.infrastructure.util;

import com.fasterxml.jackson.core.JsonParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

public abstract class AbstractExceptionHandler {
    /**
     * Method that handle and exception ocurred
     *
     * @param ex exception
     * @return Json message of error.
     */


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseObject> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        ResponseObject responseObject = new ResponseObject("error", errors.toString(), "");
        return new ResponseEntity<>(responseObject, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<ResponseObject> handleSQLException(SQLException ex) {
        String errorMessage = String.format("Error de SQL: Código: %d, Estado: %s, Mensaje: %s",
                ex.getErrorCode(), ex.getSQLState(), ex.getMessage());
        ResponseObject responseObject = new ResponseObject("error", errorMessage.toString(), "");
        return new ResponseEntity<>(responseObject, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(JsonParseException.class)
    public ResponseEntity<ResponseObject> handleJsonParseException(JsonParseException ex) {
        String errorMessage = ex.getMessage();
        ResponseObject responseObject = new ResponseObject("error", errorMessage, "");
        return new ResponseEntity<>(responseObject, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ResponseObject> handleNoSuchElementException(NoSuchElementException ex) {
        String errorMessage = ex.getMessage();
        ResponseObject responseObject = new ResponseObject("error", errorMessage, "");
        return new ResponseEntity<>(responseObject, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(UserException.class)
    public ResponseEntity<ResponseObject> handleUserException(UserException ex) {
        String errorMessage = ex.getErrorMessage();
        ResponseObject responseObject = new ResponseObject("error", errorMessage, "");
        return new ResponseEntity<>(responseObject, HttpStatus.NO_CONTENT);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ResponseObject> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        String errorMessage = ex.getCause().getLocalizedMessage();
        ResponseObject responseObject = new ResponseObject("error", errorMessage, "");
        return new ResponseEntity<>(responseObject, HttpStatus.BAD_REQUEST);
    }
}

