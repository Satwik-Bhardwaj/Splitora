package com.satwik.splitora.util;

import com.satwik.splitora.persistence.dto.ErrorDetails;
import com.satwik.splitora.persistence.dto.ErrorResponseModel;
import com.satwik.splitora.persistence.dto.ResponseModel;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public class ResponseUtil {

    public static <T> ResponseModel<T> success(T data, HttpStatus status, String message) {
        return ResponseModel.<T>builder()
                .status(status.name())
                .message(message)
                .timestamp(LocalDateTime.now())
                .data(data)
                .build();
    }

    public static ErrorResponseModel error(String message, HttpStatus status, ErrorDetails errorDetails) {
        return ErrorResponseModel.builder()
                .status(status.name())
                .message(message)
                .timestamp(LocalDateTime.now())
                .error(errorDetails)
                .build();
    }
}
