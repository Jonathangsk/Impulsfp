package com.impulsfp.server.exception;


/**
 * Excepció personalitzada per errors d'API, que inclou un codi d'error i un missatge.
 *
 * @author Jonathan Giraldo Giraldo
 */
public class ApiException extends RuntimeException {

    private ErrorCode code;

    public ApiException(ErrorCode code, String message) {
        super(message);
        this.code = code;
    }

    public ErrorCode getCode() {
        return code;
    }
}