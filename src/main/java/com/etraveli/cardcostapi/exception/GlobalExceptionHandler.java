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
     * Clase que maneja las excepciones globales.
     * Esta clase utiliza {@code @ControllerAdvice} para capturar y manejar excepciones específicas
     * de forma centralizada, permitiendo una mayor consistencia y simplicidad.
     * Extiende {@link ResponseEntityExceptionHandler} para proporcionar manejo detallado
     * de excepciones relacionadas con argumentos de métodos no válidos.
     */

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
        /**
         * Esta excepción se lanza cuando el recurso solicitado no existe en la base de datos.
         * Captura la excepción y devuelve una respuesta estructurada con el estado HTTP 404.
         *
         * @param ex      La excepción que ha sido lanzada.
         * @param request Detalles de la solicitud actual.
         * @return Una respuesta HTTP con el código de estado {@code 404 NOT FOUND} y un cuerpo con detalles del error.
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
     * Maneja excepciones relacionadas con argumentos de métodos no válidos, por ejemplo,
     * errores en la validación de {@code @RequestBody} utilizando {@code @Valid}.
     *
     * @param ex La excepción que ha sido lanzada debido a la falla en la validación.
     * @param headers Encabezados HTTP de la solicitud.
     * @param status Estado HTTP asociado al error.
     * @param request Detalles de la solicitud actual.
     * @return Una respuesta HTTP con detalles de los errores de validación y el estado correspondiente.
     */

    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", "Validation Error");

        // Mapea los errores de validación y agrega al cuerpo de la respuesta
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));
        body.put("errors", errors);

        return new ResponseEntity<>(body, headers, status);
    }

    /**
     * Maneja excepciones generales que no están específicamente controladas en otros manejadores.
     *
     * @param ex      La excepción lanzada.
     * @param request Detalles de la solicitud actual.
     * @return Una respuesta HTTP con el código de estado {@code 500 INTERNAL SERVER ERROR} y detalles del error.
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
