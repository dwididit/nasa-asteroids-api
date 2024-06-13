package com.dwidi.nasaasteroidsapi.exception;

import com.dwidi.nasaasteroidsapi.dto.ExceptionResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponseDTO<String>> handleGlobalException(Exception e, WebRequest request) {
        String path = request.getDescription(false);
        log.error("An error occurred at path: {}", path, e);
        ExceptionResponseDTO<String> response = new ExceptionResponseDTO<>(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                e.getMessage(),
                "An unexpected error occurred"
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AsteroidNotFoundException.class)
    public ResponseEntity<ExceptionResponseDTO<String>> handleAsteroidNotFoundException(AsteroidNotFoundException e, WebRequest request) {
        String path = request.getDescription(false);
        log.error("Asteroid not found at path: {}", path);
        ExceptionResponseDTO<String> response = new ExceptionResponseDTO<>(
                HttpStatus.NOT_FOUND.value(),
                e.getMessage(),
                "Asteroid not found with the provided ID"
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ExceptionResponseDTO<String>> handleBadRequest(BadRequestException e, WebRequest request) {
        String path = request.getDescription(false);
        log.error("An error occurred at path: {}", path, e);
        ExceptionResponseDTO<String> response = new ExceptionResponseDTO<>(
                HttpStatus.BAD_REQUEST.value(),
                e.getMessage(),
                "End date must be 7 days after start_date"
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DateFormatException.class)
    public ResponseEntity<ExceptionResponseDTO<String>> dateFormatException(BadRequestException e, WebRequest request) {
        String path = request.getDescription(false);
        log.error("An error occurred at path: {}", path, e);
        ExceptionResponseDTO<String> response = new ExceptionResponseDTO<>(
                HttpStatus.BAD_REQUEST.value(),
                e.getMessage(),
                "Date must on format YYYY-MM-DD"
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
