package com.etraveli.cardcostapi.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Class that handles global exceptions.
 * This class uses {@code @ControllerAdvice} to capture and manage specific exceptions
 * in a centralized way, ensuring greater consistency and simplicity.
 * Extends {@link ResponseEntityExceptionHandler} to provide detailed handling
 * of exceptions related to invalid method arguments.
 */
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    /**
     * This exception is thrown when the requested resource does not exist in the database.
     * Captures the exception and returns a structured response with HTTP status 404.
     * @param ex The exception that was thrown.
     * @param request Details of the current request.
     * @return An HTTP response with status code {@code 404 NOT FOUND} and a body with error details.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.NOT_FOUND.value());
        body.put("error", "Not Found");
        body.put("message", ex.getMessage());
        body.put("path", request.getDescription(false));

        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    /**
     * Handles exceptions related to invalid method arguments, for example,
     * validation errors in {@code @RequestBody} using {@code @Valid}.
     * @param ex The exception that was thrown due to validation failure.
     * @param headers HTTP headers of the request.
     * @param status  HTTP status associated with the error.
     * @param request Details of the current request.
     * @return An HTTP response with validation error details and the corresponding status.
     */
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", "Validation Error");

        // Map validation errors and add them to the response body
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));
        body.put("errors", errors);

        return new ResponseEntity<>(body, headers, status);
    }

    /**
     * Handles general exceptions that are not specifically controlled by other handlers.
     * @param ex The exception that was thrown.
     * @param request Details of the current request.
     * @return An HTTP response with status code {@code 500 INTERNAL SERVER ERROR} and error details.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGeneralException(Exception ex, WebRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        body.put("error", "Internal Server Error");
        body.put("message", ex.getMessage());
        body.put("path", request.getDescription(false));

        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
