package com.satwik.splitora.persistence.dto.group;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupListDTOWithin {

    @NotNull
    @NotBlank
    private UUID groupId;

    @NotNull
    private String groupName;

}