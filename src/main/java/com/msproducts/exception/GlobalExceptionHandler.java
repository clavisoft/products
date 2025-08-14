package com.msproducts.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.validation.ConstraintViolationException;

@ControllerAdvice
public class GlobalExceptionHandler {

  private static final String TIMESTAMP = "timestamp";
  private static final String STATUS = "status";
  private static final String ERROR = "error";
  private static final String MESSAGE = "message";

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<Map<String, Object>> handleResourceNotFound(ResourceNotFoundException ex) {
    Map<String, Object> body = new HashMap<>();
    body.put(TIMESTAMP, LocalDateTime.now());
    body.put(STATUS, HttpStatus.NOT_FOUND.value());
    body.put(ERROR, "Recurso no encontrado");
    body.put(MESSAGE, ex.getMessage());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Map<String, Object>> handleGeneralException(Exception ex) {
    Map<String, Object> body = new HashMap<>();
    body.put(TIMESTAMP, LocalDateTime.now());
    body.put(STATUS, HttpStatus.INTERNAL_SERVER_ERROR.value());
    body.put(ERROR, "Error interno");
    body.put(MESSAGE, ex.getMessage());
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, Object>> handleValidationErrors(MethodArgumentNotValidException ex) {
    Map<String, Object> body = new HashMap<>();
    Map<String, String> errors = new HashMap<>();

    ex.getBindingResult().getFieldErrors().forEach(error ->
      errors.put(error.getField(), error.getDefaultMessage())
    );

    body.put(TIMESTAMP, LocalDateTime.now());
    body.put(STATUS, HttpStatus.BAD_REQUEST.value());
    body.put(ERROR, "Validación fallida");
    body.put("messages", errors);

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<Map<String, Object>> handleConstraintViolation(ConstraintViolationException ex) {
    Map<String, Object> body = new HashMap<>();
    Map<String, String> errors = new HashMap<>();

    ex.getConstraintViolations().forEach(violation -> {
      String field = violation.getPropertyPath().toString();
      String message = violation.getMessage();
      errors.put(field, message);
    });

    body.put(TIMESTAMP, LocalDateTime.now());
    body.put(STATUS, HttpStatus.BAD_REQUEST.value());
    body.put(ERROR, "Validación fallida");
    body.put("messages", errors);

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
  }
  
}
