package com.satwik.splitora.service.interfaces;

import com.satwik.splitora.persistence.dto.user.AuthenticationResponse;

public interface OAuthService {
    public AuthenticationResponse handleCallback(String code, String state);
}
