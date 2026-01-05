//Like a security guard that sits on all your components, wait for any error that pushes out to catch.
package com.smartparking.smart_parking_backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

//Global handler - Main spring annotation. Says catch error anywhere in the application
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class) // Listener for specific error. Here, method...tion is exact
                                                       // error that throws when @valid checks and fails
    // Container - that has entire obj and HTTP status
    public ResponseEntity<String> handleNotFound(ResourceNotFoundException ex) {
        // String errorMessage = ex.getBindingResult()
        // .getFieldErrors()
        // .get(0)
        // .getDefaultMessage();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(SlotAlreadyBookedException.class)
    public ResponseEntity<String> handleSlotBooked(SlotAlreadyBookedException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneral(Exception ex) {
        ex.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Something went wrong");
    }
}