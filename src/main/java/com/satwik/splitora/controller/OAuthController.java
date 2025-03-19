package com.satwik.splitora.controller;

import com.satwik.splitora.persistence.dto.user.AuthenticationResponse;
import com.satwik.splitora.service.interfaces.OAuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/oauth2")
public class OAuthController {

    @Autowired
    OAuthService oAuthService;

    /**
     * Handles the OAuth2 callback.
     *
     * This endpoint processes the OAuth2 callback by handling the authorization code and state parameters.
     * It logs the incoming request and the resulting response.
     *
     * @param code the authorization code received from the OAuth2 provider.
     * @param state the state parameter received from the OAuth2 provider, used to prevent CSRF attacks.
     * @return a ResponseEntity containing the AuthenticationResponse after handling the OAuth2 callback.
     */
    @GetMapping("/callback")
    public ResponseEntity<AuthenticationResponse> handleCallback(@RequestParam("code") String code, @RequestParam("state") String state) {
        // TODO : Match state
        log.info("Get Endpoint: oAuth callback with code : {}, state : {}", code, state);
        AuthenticationResponse response = oAuthService.handleCallback(code, state);
        log.info("Get Endpoint: callback response: {}", response);
        return ResponseEntity.ok(response);
    }
}
