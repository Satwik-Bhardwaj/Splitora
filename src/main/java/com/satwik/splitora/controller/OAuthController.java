package com.satwik.splitora.controller;

import com.satwik.splitora.persistence.dto.ResponseModel;
import com.satwik.splitora.persistence.dto.user.AuthenticationResponse;
import com.satwik.splitora.service.interfaces.OAuthService;
import com.satwik.splitora.util.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
     * @return a ResponseEntity containing a ResponseModel with the AuthenticationResponse after handling the OAuth2 callback.
     */
    @GetMapping("/callback")
    public ResponseEntity<ResponseModel<AuthenticationResponse>> handleCallback(@RequestParam("code") String code, @RequestParam("state") String state) {
        log.info("Get Endpoint: oAuth callback with code : {}, state : {}", code, state);
        AuthenticationResponse response = oAuthService.handleCallback(code, state);
        ResponseModel<AuthenticationResponse> responseModel = ResponseUtil.success(response, HttpStatus.OK, "OAuth2 callback handled successfully");
        log.info("Get Endpoint: callback response: {}", responseModel);
        return ResponseEntity.status(HttpStatus.OK).body(responseModel);
    }
}