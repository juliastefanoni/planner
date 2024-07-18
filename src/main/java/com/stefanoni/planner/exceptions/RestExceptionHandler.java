package com.stefanoni.planner.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler({
            InvalidFieldsException.class
    })
    public ResponseEntity<ApiError> InvalidFieldsException(RuntimeException ex) {
        ApiError apiError = new ApiError(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.name(),
                List.of(ex.getMessage()));

        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({
            NotFoundException.class
    })
    public ResponseEntity<ApiError> notFoundException(RuntimeException ex) {
        ApiError apiError = new ApiError(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.name(),
                List.of(ex.getMessage()));

        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }
}
