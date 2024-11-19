package com.etraveli.cardcostapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import java.io.Serial;

/**
 * Exception thrown when a requested resource is not found.
 * This exception is annotated with {@code @ResponseStatus(HttpStatus.NOT_FOUND)},
 * meaning that whenever it is thrown, the HTTP response status will be set to {@code 404 NOT FOUND}.
 * It extends {@link RuntimeException}, allowing it to be used for unchecked exception handling.
 *
 * @see RuntimeException
 * @see org.springframework.web.bind.annotation.ResponseStatus
 */

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public ResourceNotFoundException(String message) {
        super(message);
    }
}

