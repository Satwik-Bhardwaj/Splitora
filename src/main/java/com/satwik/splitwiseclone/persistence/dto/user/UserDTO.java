package com.satwik.splitwiseclone.persistence.dto.user;


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

    UserDTO(UUID userId, String username, String email, String countryCode, Long phoneNumber) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.phone = new PhoneDTO(countryCode, phoneNumber);
    }
}
