package com.satwik.splitora.configuration.security;

import com.satwik.splitora.constants.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class LoggedInUser {

    private String userEmail;
    private UUID userId;
    private UserRole role;

    public boolean hasRole(UserRole role) {
        return this.role.equals(role);
    }
}
