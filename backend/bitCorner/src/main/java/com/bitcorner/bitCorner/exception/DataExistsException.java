package com.bitcorner.bitCorner.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class DataExistsException extends ServiceRuntimeException {

    private ResponseEntity responseEntity;

    public DataExistsException(String entity, String id) {
        super(HttpStatus.CONFLICT, 1016, generateErrorDetails(entity, id));
    }

    private static String generateErrorDetails(String entity, String id) {
        return String.format("Entity '%s' with identifier '%s' already existed.", entity, id);
    }

    public DataExistsException(String entity, String id, String id2) {
        super(HttpStatus.CONFLICT, 1016, generateErrorDetails2(entity, id, id2));
    }

    private static String generateErrorDetails2(String entity, String id, String id2) {
        return String.format(
                "Entity '%s' with identifier '%s' and '%s' already existed.",
                entity, id, id2
        );
    }
}

