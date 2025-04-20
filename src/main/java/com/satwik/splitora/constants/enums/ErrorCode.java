package com.satwik.splitora.constants.enums;

public enum ErrorCode {

    // Common errors
    UNKNOWN_ERROR(1000, "An unknown error occurred."),
    INVALID_REQUEST(1001, "The request is invalid."),

    // User-related errors
    USER_NOT_FOUND(2001, "User not found."),
    USER_ALREADY_EXISTS(2002, "User already exists."),
    USER_UNAUTHORIZED(2003, "User is not authorized."),

    // Authentication-related errors
    AUTHENTICATION_FAILED(3001, "Authentication failed."),
    ACCESS_DENIED(3002, "Access denied."),
    ACCESS_TOKEN_INVALID(3003, "Access token expired or invalid. Use refresh token to obtain a new access token."),
    REFRESH_TOKEN_INVALID(3004, "Refresh token is invalid or expired. Login again!"),

    // Validation errors
    VALIDATION_FAILED(4001, "Validation failed."),
    MISSING_REQUIRED_FIELD(4002, "Missing required field: {0}"),

    // Database-related errors
    DATABASE_ERROR(5001, "A database error occurred."),
    ENTITY_NOT_FOUND(5002, "Entity not found."),

    // Network-related errors
    NETWORK_ERROR(6001, "A network error occurred.");

    private final int code;
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getFormattedMessage(Object... args) {
        return String.format(message, args);
    }
}

