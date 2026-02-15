package com.example.Repetition_7.exception;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiErrorResponse> handleIllegalArgumentException(IllegalArgumentException e) {
        ApiErrorResponse response = new ApiErrorResponse();

        response.setMessage(e.getMessage());

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiErrorResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        ApiErrorResponse response = new ApiErrorResponse();

        response.setMessage("Invalid value for parameter '" + e.getName() + "' : '" + e.getValue() + "'");

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        ApiErrorResponse response = new ApiErrorResponse();
        Map<String, String> errors = new HashMap<>();

        for(var error : e.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

        response.setMessage("Validation failed");
        response.setErrors(errors);

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleTaskNotFoundException(TaskNotFoundException e) {
        ApiErrorResponse response = new ApiErrorResponse();

        response.setMessage(e.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleConstraintViolationException(ConstraintViolationException e) {
        ApiErrorResponse response = new ApiErrorResponse();
        Map<String, String> errors = new HashMap<>();

        for(var error : e.getConstraintViolations()) {
            Path.Node last = null;
            for(Path.Node node : error.getPropertyPath()) {
                last = node;
            }
            String paramName = last != null && last.getName() != null ? last.getName() : "unknown";
            errors.put(paramName, error.getMessage());
        }

        response.setMessage("Validation failed");
        response.setErrors(errors);

        return ResponseEntity.badRequest().body(response);
    }
}
