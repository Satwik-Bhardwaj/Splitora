package com.satwik.splitora.controller;

import com.satwik.splitora.configuration.jwt.JwtUtil;
import com.satwik.splitora.persistence.dto.user.AuthenticationResponse;
import com.satwik.splitora.persistence.dto.user.LoginRequest;
import com.satwik.splitora.persistence.dto.user.RefreshTokenRequest;
import com.satwik.splitora.repository.UserRepository;
import com.satwik.splitora.service.interfaces.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
     * This endpoint processes the login request by authenticating the user
     * with the provided login details. It logs the incoming request and the
     * resulting authentication response.
     *
     * @param loginRequest the request body containing the user's login credentials.
     * @return a ResponseEntity containing the authentication response, which includes
     *         the authentication token and user details if the login is successful.
     */
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> loginUser(@RequestBody LoginRequest loginRequest) {
        log.info("Post Endpoint: login user with request: {}", loginRequest);
        AuthenticationResponse response = authService.authenticateUser(loginRequest);
        log.info("Post Endpoint: login user with response: {}", response);
        return ResponseEntity.ok(response);
    }

    /**
     * Handles the refresh token request for a user.
     *
     * This endpoint processes the refresh token request by generating a new
     * authentication token using the provided refresh token details. It logs the
     * incoming request and the resulting authentication response.
     *
     * @param refreshTokenRequest the request body containing the refresh token details.
     * @return a ResponseEntity containing the authentication response, which includes
     *         the new authentication token and user details if the refresh token is valid.
     */
    @PostMapping("/refresh_token")
    public ResponseEntity<AuthenticationResponse> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        log.info("Post Endpoint: refresh token generation with request: {}", refreshTokenRequest);
        AuthenticationResponse response = authService.issueNewToken(refreshTokenRequest);
        log.info("Post Endpoint: refresh token generation with response: {}", response);
        return ResponseEntity.ok(response);
    }
}