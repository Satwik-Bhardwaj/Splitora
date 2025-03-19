package com.satwik.splitwiseclone.persistence.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OwerDTO {

    UUID userId;

    String username;

    double amount;

}
