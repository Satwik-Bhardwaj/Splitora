package com.satwik.splitora.persistence.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Error API response")
public class ErrorResponseModel {

    @Schema(description = "HTTP status", example = "BAD_REQUEST")
    private String status;

    @Schema(description = "Error message", example = "Invalid input")
    private String message;

    @Schema(description = "Time at which the error occurred", example = "2025-04-20T12:34:56")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;

    @Schema(description = "Error details")
    private ErrorDetails error;
}
