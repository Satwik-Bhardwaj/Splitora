package com.satwik.splitora.persistence.dto.user;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PhoneDTO {

    @NotNull
    @Pattern(regexp = "\\+[0-9]+")
    String countryCode;

    @NotNull
    Long phoneNumber;

}
