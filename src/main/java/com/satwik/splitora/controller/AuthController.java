package com.satwik.splitora.controller;

import com.satwik.splitora.configuration.jwt.JwtUtil;
import com.satwik.splitora.persistence.dto.ResponseModel;
import com.satwik.splitora.persistence.dto.user.AuthenticationResponse;
import com.satwik.splitora.persistence.dto.user.LoginRequest;
import com.satwik.splitora.persistence.dto.user.RefreshTokenRequest;
import com.satwik.splitora.repository.UserRepository;
import com.satwik.splitora.service.interfaces.AuthService;
import com.satwik.splitora.util.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * Handles the login request for a user.
     *
     * @param loginRequest the request body containing the user's login credentials.
     * @return a ResponseEntity containing a ResponseModel with the authentication response.
     */
    @PostMapping("/login")
    public ResponseEntity<ResponseModel<AuthenticationResponse>> loginUser(@RequestBody LoginRequest loginRequest) {
        log.info("Post Endpoint: login user with request: {}", loginRequest);
        AuthenticationResponse response = authService.authenticateUser(loginRequest);
        ResponseModel<AuthenticationResponse> responseModel = ResponseUtil.success(response, HttpStatus.OK, "User logged in successfully");
        log.info("Post Endpoint: login user with response: {}", responseModel);
        return ResponseEntity.ok(responseModel);
    }

    /**
     * Handles the refresh token request for a user.
     *
     * @param refreshTokenRequest the request body containing the refresh token details.
     * @return a ResponseEntity containing a ResponseModel with the authentication response.
     */
    @PostMapping("/refresh_token")
    public ResponseEntity<ResponseModel<AuthenticationResponse>> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        log.info("Post Endpoint: refresh token generation with request: {}", refreshTokenRequest);
        AuthenticationResponse response = authService.issueNewToken(refreshTokenRequest);
        ResponseModel<AuthenticationResponse> responseModel = ResponseUtil.success(response, HttpStatus.OK, "Token refreshed successfully");
        log.info("Post Endpoint: refresh token generation with response: {}", responseModel);
        return ResponseEntity.ok(responseModel);
    }
}