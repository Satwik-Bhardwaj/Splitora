package com.satwik.splitora.constants.enums;

public enum UserRole {
    USER,
    ADMIN,
    TESTER;

    public static UserRole fromString(String role) {
        try {
            return UserRole.valueOf(role.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid role: " + role);
        }
    }
}
