package com.satwik.splitora.controller;

import com.satwik.splitora.persistence.dto.ResponseModel;
import com.satwik.splitora.util.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/health")
public class HealthController {

    @RequestMapping(value = "/ping", method = RequestMethod.HEAD)
    public ResponseEntity<Void> ping() {
        log.info("HEAD Endpoint: PING. Returning PONG");
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/ping", method = {RequestMethod.GET})
    public ResponseEntity<ResponseModel<String>> pingGet() {
        log.info("Get Endpoint: PING. Returning PONG");
        ResponseModel<String> responseModel = ResponseUtil.success("PONG", HttpStatus.OK, "Health check successful");
        return ResponseEntity.status(HttpStatus.OK).body(responseModel);
    }
}