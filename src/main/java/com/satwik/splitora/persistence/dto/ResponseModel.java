package com.satwik.splitora.persistence.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Successful API response")
public class ResponseModel<T> {

    @Schema(description = "HTTP status", example = "OK")
    private String status;

    @Schema(description = "Success message", example = "Operation completed successfully")
    private String message;

    @Schema(description = "Time at which the response was generated", example = "2025-04-20T12:34:56")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;

    @Schema(description = "Response data")
    private T data;
}
