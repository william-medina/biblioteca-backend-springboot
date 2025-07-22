package com.williammedina.biblioteca.infrastructure.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Comparator;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentialsException(BadCredentialsException ex) {
        ErrorResponse errorResponse = new ErrorResponse("Password incorrecto.");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ErrorResponse> handleAppException(AppException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage());
        return ResponseEntity.status(ex.getHttpStatus()).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnknownHostException(Exception ex) {
        ErrorResponse errorResponse = new ErrorResponse("Internal Server Error: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    // Manejo de errores de validación de formulario (MethodArgumentNotValidException)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {

        //O rden jerárquico de los campos
        List<String> fieldPriorityOrder = List.of("email", "password", "isbn", "title", "author", "publisher" ,"publicationYear", "location");

        ErrorResponse error = exception.getFieldErrors()
                .stream()
                .sorted(Comparator.comparingInt(fieldError -> {
                    int index = fieldPriorityOrder.indexOf(fieldError.getField());
                    return index == -1 ? Integer.MAX_VALUE : index;
                }))
                .map(fieldError -> new ErrorResponse(fieldError.getDefaultMessage()))
                .findFirst()
                .orElse(null);

        if (error != null) {
            return ResponseEntity.badRequest().body(error);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
}
