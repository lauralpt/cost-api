package com.etraveli.cardcostapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import java.io.Serial;

//Excepción lanzada cuando un recurso solicitado no se encuentra en el sistema.

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;
    public ResourceNotFoundException(String message) {
        super(message);
    }
}

