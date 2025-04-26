package com.satwik.splitora.persistence.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Detailed error information")
public class ErrorDetails {

    @Schema(description = "Error code", example = "4001")
    private int code;

    @Schema(description = "Human-readable description", example = "Validation failed for the request")
    private String description;
}
