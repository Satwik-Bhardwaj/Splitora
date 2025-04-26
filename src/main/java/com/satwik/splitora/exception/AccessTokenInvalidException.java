package com.satwik.splitora.exception;

public class AccessTokenInvalidException extends RuntimeException {
    public AccessTokenInvalidException() {
        super();
    }
    public AccessTokenInvalidException(String message) {
        super(message);
    }

    public AccessTokenInvalidException(String message, Throwable cause) {
        super(message, cause);
    }
}
