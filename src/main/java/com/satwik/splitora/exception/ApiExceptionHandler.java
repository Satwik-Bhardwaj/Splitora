package com.satwik.splitora.exception;

import com.satwik.splitora.constants.enums.ErrorCode;
import com.satwik.splitora.persistence.dto.ErrorDetails;
import com.satwik.splitora.persistence.dto.ResponseModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ResponseModel<String>> handleBadRequestException(RuntimeException ex) {
        log.info("Runtime exception occurred: ", ex);
        ResponseModel<String> errorResponse = new ResponseModel<>();
        errorResponse.setStatus(HttpStatus.BAD_REQUEST);
        errorResponse.setTimestamp(LocalDateTime.now());
        errorResponse.setErrors(List.of(ErrorDetails.builder()
                .errorCode(ErrorCode.UNKNOWN_ERROR.getCode())
                .errorDisc(ErrorCode.UNKNOWN_ERROR.getMessage()).build()));

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<ResponseModel<String>> handleDataNotFoundException(DataNotFoundException ex) {
        log.info("DataNotFoundException occurred: ", ex);
        ResponseModel<String> errorResponse = new ResponseModel<>();
        errorResponse.setStatus(HttpStatus.NOT_FOUND);
        errorResponse.setTimestamp(LocalDateTime.now());
        errorResponse.setErrors(List.of(ErrorDetails.builder()
                .errorCode(ErrorCode.ENTITY_NOT_FOUND.getCode())
                .errorDisc(ErrorCode.ENTITY_NOT_FOUND.getMessage()).build()));

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FailedToSaveException.class)
    public ResponseEntity<ResponseModel<String>> handleFailedToSaveException(FailedToSaveException ex) {
        log.info("FailedToSaveException occurred: ", ex);
        ResponseModel<String> errorResponse = new ResponseModel<>();
        errorResponse.setStatus(HttpStatus.BAD_REQUEST);
        errorResponse.setTimestamp(LocalDateTime.now());
        errorResponse.setErrors(List.of(ErrorDetails.builder()
                .errorCode(ErrorCode.ENTITY_NOT_FOUND.getCode())
                .errorDisc(ErrorCode.ENTITY_NOT_FOUND.getMessage()).build()));

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ResponseModel<String>> handleAccessDeniedException(FailedToSaveException ex) {
        log.info("AccessDeniedException occurred: ", ex);
        ResponseModel<String> errorResponse = new ResponseModel<>();
        errorResponse.setStatus(HttpStatus.FORBIDDEN);
        errorResponse.setTimestamp(LocalDateTime.now());
        errorResponse.setErrors(List.of(ErrorDetails.builder()
                .errorCode(ErrorCode.ACCESS_DENIED.getCode())
                .errorDisc(ErrorCode.ACCESS_DENIED.getMessage()).build()));

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseModel<String>> handleServiceCommunicationException(Exception ex) {
        log.error("ServiceCommunicationException handled with message: ", ex);
        ResponseModel<String> errorResponse = new ResponseModel<>();
        errorResponse.setStatus(HttpStatus.NOT_FOUND);
        errorResponse.setTimestamp(LocalDateTime.now());
        errorResponse.setErrors(List.of(ErrorDetails.builder()
                .errorCode(ErrorCode.UNKNOWN_ERROR.getCode())
                .errorDisc(ErrorCode.UNKNOWN_ERROR.getMessage()).build()));
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
}
