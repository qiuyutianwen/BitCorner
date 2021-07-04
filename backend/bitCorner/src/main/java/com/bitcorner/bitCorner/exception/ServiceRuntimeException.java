package com.bitcorner.bitCorner.exception;

import org.springframework.http.HttpStatus;

public class ServiceRuntimeException extends RuntimeException {

    private static final long serialVersionUID = -6092760518071842577L;
    public static final int ERROR_ID = 1000;
    private final HttpStatus httpStatus;
    private final int errorIdentifier;
    private final String errorDetails;

    public ServiceRuntimeException(String errorMessage) {
        this(errorMessage, (Throwable) null);
    }

    public ServiceRuntimeException(String errorMessage, Throwable cause) {
        this(HttpStatus.INTERNAL_SERVER_ERROR, 1000, generateErrorDetails(), errorMessage, cause);
    }

    protected ServiceRuntimeException(
            HttpStatus httpStatus, int errorIdentifier, String errorDetails) {
        this(httpStatus, errorIdentifier, errorDetails, errorDetails, (Throwable) null);
    }

    protected ServiceRuntimeException(
            HttpStatus httpStatus, int errorIdentifier, String errorDetails, String errorMessage) {
        this(httpStatus, errorIdentifier, errorDetails, errorMessage, (Throwable) null);
    }

    protected ServiceRuntimeException(
            HttpStatus httpStatus,
            int errorIdentifier,
            String errorDetails,
            String errorMessage,
            Throwable cause) {
        super(errorMessage, cause);
        this.httpStatus = httpStatus;
        this.errorIdentifier = errorIdentifier;
        this.errorDetails = errorDetails;
    }

    public HttpStatus getHttpStatus() {
        return this.httpStatus;
    }

    public int getErrorIdentifier() {
        return this.errorIdentifier;
    }

    public String getSummary() {
        return this.httpStatus.getReasonPhrase();
    }

    public String getDetails() {
        return this.errorDetails;
    }

    private static String generateErrorDetails() {
        return "The server has encountered an internal error that prevents it from fulfilling this request (%s). Please try again later or contact the administrator.";
    }
}
