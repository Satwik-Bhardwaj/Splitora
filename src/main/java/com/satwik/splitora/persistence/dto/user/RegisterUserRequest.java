package com.satwik.splitora.persistence.dto.user;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterUserRequest {

    @NotNull
    String username;

    @NotNull
    @Email(message = "Enter valid email address")
    String email;

    @NotNull
    @Valid
    PhoneDTO phone;

    @NotNull
    @Size(min = 8, message = "Password should at least have 8 characters length")
    String password;

}
