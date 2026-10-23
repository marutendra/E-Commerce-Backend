package com.Learning.User.ExceptionHandling;

import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<Map<String, String>> build(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(Map.of("error", message));
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Map<String, String>> notFound(NoSuchElementException e) {
        return build(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> badRequest(IllegalArgumentException e) {
        return build(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    // Seller service answered 404 (e.g. product id does not exist)
    @ExceptionHandler(FeignException.NotFound.class)
    public ResponseEntity<Map<String, String>> feignNotFound(FeignException.NotFound e) {
        return build(HttpStatus.NOT_FOUND, "Product not found");
    }

    // Seller service is down or returned an error
    @ExceptionHandler(FeignException.class)
    public ResponseEntity<Map<String, String>> feignError(FeignException e) {
        return build(HttpStatus.SERVICE_UNAVAILABLE, "Seller service is not available right now");
    }
}
