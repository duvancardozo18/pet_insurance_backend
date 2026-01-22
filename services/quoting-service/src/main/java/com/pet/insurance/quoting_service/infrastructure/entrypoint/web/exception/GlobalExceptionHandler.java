package com.pet.insurance.quoting_service.infrastructure.entrypoint.web.exception;

import com.pet.insurance.quoting_service.domain.exception.InvalidPetAgeException;
import com.pet.insurance.quoting_service.domain.exception.QuotationNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(QuotationNotFoundException.class)
    public Mono<ResponseEntity<Map<String, Object>>> handleQuotationNotFoundException(QuotationNotFoundException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.NOT_FOUND.value());
        body.put("error", "Not Found");
        body.put("message", ex.getMessage());

        return Mono.just(new ResponseEntity<>(body, HttpStatus.NOT_FOUND));
    }

    @ExceptionHandler(InvalidPetAgeException.class)
    public Mono<ResponseEntity<Map<String, Object>>> handleInvalidPetAgeException(InvalidPetAgeException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Bad Request");
        body.put("message", ex.getMessage());

        return Mono.just(new ResponseEntity<>(body, HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public Mono<ResponseEntity<Map<String, Object>>> handleIllegalArgumentException(IllegalArgumentException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Bad Request");
        body.put("message", ex.getMessage());

        return Mono.just(new ResponseEntity<>(body, HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<Map<String, Object>>> handleGeneralException(Exception ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        body.put("error", "Internal Server Error");
        body.put("message", "An unexpected error occurred");

        return Mono.just(new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR));
    }
}
