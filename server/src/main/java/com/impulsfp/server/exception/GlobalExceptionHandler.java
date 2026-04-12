package com.impulsfp.server.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;


/**
 * Controlador d'excepcions global per gestionar les excepcions personalitzades de l'API i retornar respostes d'error coherents.
 *
 * @author Jonathan Giraldo Giraldo
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<?> handleApiException(ApiException ex) {

        return ResponseEntity.status(400).body(
                Map.of(
                        "error", ex.getMessage(),
                        "code", ex.getCode()
                )
        );
    }
}