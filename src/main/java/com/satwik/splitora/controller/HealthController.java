package com.satwik.splitora.controller;

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

    @RequestMapping(value = "/ping", method = {RequestMethod.HEAD, RequestMethod.GET})
    public ResponseEntity<String> ping() {
        log.info("Get Endpoint: PING. Returning PONG");
        return ResponseEntity.status(HttpStatus.OK).body("PONG");
    }
}
