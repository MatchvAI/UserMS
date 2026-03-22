package com.UserMS.exceptions;

import com.UserMS.dtos.StandardResponseStructure;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.context.support.DefaultMessageSourceResolvable;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(UserException.class)
    public ResponseEntity<StandardResponseStructure> handleUserException(UserException ex) {
        StandardResponseStructure response = StandardResponseStructure.error(
            String.valueOf(HttpStatus.CONFLICT.value()),
            ex.getErrorCode(),
            ex.getMessage()
        );
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<StandardResponseStructure> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(DefaultMessageSourceResolvable::getDefaultMessage)
            .collect(Collectors.joining(", "));
        
        StandardResponseStructure response = StandardResponseStructure.error(
            String.valueOf(HttpStatus.BAD_REQUEST.value()),
            "VALIDATION_ERROR",
            errorMessage
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<StandardResponseStructure> handleGenericException(Exception ex) {
        StandardResponseStructure response = StandardResponseStructure.error(
            String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
            "INTERNAL_SERVER_ERROR",
            "An unexpected error occurred: " + ex.getMessage()
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
