package com.satwik.splitora.service.interfaces;

import com.satwik.splitora.persistence.dto.user.AuthenticationResponse;
import com.satwik.splitora.persistence.dto.user.LoginRequest;
import com.satwik.splitora.persistence.dto.user.RefreshTokenRequest;

public interface AuthService {
    public AuthenticationResponse authenticateUser(LoginRequest loginRequest);
    public AuthenticationResponse issueNewToken(RefreshTokenRequest refreshTokenRequest);
}
