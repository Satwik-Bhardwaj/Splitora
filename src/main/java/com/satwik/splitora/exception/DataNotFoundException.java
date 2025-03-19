package com.satwik.splitora.exception;

public class DataNotFoundException extends RuntimeException {

    public DataNotFoundException() {
        super();
    }
    public DataNotFoundException(String message) {
        super(message);
    }

    public DataNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}
