package com.bitcorner.bitCorner.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class UnauthorizedOperationException extends ServiceRuntimeException {
    private ResponseEntity responseEntity;

    public UnauthorizedOperationException(String msg) {
        super(HttpStatus.UNAUTHORIZED, 1016, msg);
    }

}
