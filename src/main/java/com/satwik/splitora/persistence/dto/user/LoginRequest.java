package com.satwik.splitora.persistence.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    @NotBlank(message = "User email should not be blank or null")
    String userEmail;

    @NotBlank(message = "Password should not be null or empty.")
    @Size(min = 8)
    String password;

}
