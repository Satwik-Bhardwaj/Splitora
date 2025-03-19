package com.satwik.splitora.persistence.dto.group;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupListDTO {

    @NotNull
    @NotBlank
    private String owner;

    @NotNull
    public List<GroupListDTOWithin> groups;

}

