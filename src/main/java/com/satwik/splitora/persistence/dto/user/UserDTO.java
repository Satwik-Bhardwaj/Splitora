package com.satwik.splitora.persistence.dto.user;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    @NotNull
    UUID userId;

    @NotNull
    String username;

    @NotNull
    String email;

    @NotNull
    PhoneDTO phone;
}
