package com.example.stundenplan.web;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class RestExceptionHandler {

  @ExceptionHandler(ConflictException.class)
  public ResponseEntity<?> handleConflict(ConflictException ex) {
    return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiError("CONFLICT", ex.getMessage()));
  }

  @ExceptionHandler(org.springframework.web.bind.MethodArgumentNotValidException.class)
  public ResponseEntity<?> handleValidation(org.springframework.web.bind.MethodArgumentNotValidException ex) {
    var first = ex.getBindingResult().getFieldErrors().stream().findFirst();
    String msg = first.map(e -> e.getField() + " " + e.getDefaultMessage()).orElse("Validation error");
    return ResponseEntity.badRequest().body(new ApiError("BAD_REQUEST", msg));
  }


  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<?> handleBadRequest(IllegalArgumentException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiError("BAD_REQUEST", ex.getMessage()));
  }

  record ApiError(String type, String message) {}
}

