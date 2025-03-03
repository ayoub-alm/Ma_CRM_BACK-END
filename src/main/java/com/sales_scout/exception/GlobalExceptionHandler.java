package com.sales_scout.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", HttpStatus.NOT_FOUND.value());
        response.put("message", ex.getMessage());
        response.put("resource", ex.getResourceName());
        response.put("field", ex.getFieldName());
        response.put("value", ex.getFieldValue());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<Object> handleDataNotFoundException(DataNotFoundException ex , WebRequest request){
        Map<String, Object> response = new HashMap<>();
        response.put("status",HttpStatus.NOT_FOUND.value());
        response.put("message", ex.getMessage());
        response.put("code", ex.getCode());
        return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(DuplicateKeyValueException.class)
    public ResponseEntity<Object> handleDuplicateKeyValueException(DuplicateKeyValueException ex , WebRequest request){
        Map<String, Object> response = new HashMap<>();
        response.put("status",HttpStatus.CONFLICT.value());
        response.put("message", ex.getMessage());
        response.put("cause",ex.getCause());
        return new ResponseEntity<>(response,HttpStatus.CONFLICT);
    }

    // You can also handle other exceptions if needed

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<Object> handleUserAlreadyExistsException(UserAlreadyExistsException ex , WebRequest request){
        Map<String, Object> response = new HashMap<>();
        response.put("status",HttpStatus.FOUND.value());
        response.put("message", ex.getMessage());
        return new ResponseEntity<>(response,HttpStatus.FOUND);
    }

    @ExceptionHandler(DataAlreadyExistsException.class)
        public ResponseEntity<Object> handleDataAlreadyExistsException(DataAlreadyExistsException ex , WebRequest request){
        Map<String, Object> response = new HashMap<>();
        response.put("status",HttpStatus.FOUND.value());
        response.put("message", ex.getMessage());
        response.put("cause",ex.getCause());
        return new ResponseEntity<>(response,HttpStatus.FOUND);
    }


    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException e) {
        return ResponseEntity.status(500).body("Erreur interne du serveur : " + e.getMessage());
    }
}
