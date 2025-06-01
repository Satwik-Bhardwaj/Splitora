package com.satwik.splitora.constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SecurityConstants {
    public static final List<String> WHITELISTED_URLS = new ArrayList<>(Arrays.asList(
            "/api/v1/auth/login",
            "/api/v1/auth/refresh_token",
            "/api/v1/user/register",
            "/api/v1/auth/getUser",
            "/api/v1/oauth2/login",
            "/api/v1/oauth2/callback",
            "/api/v1/health/ping",
            "/v3/api-docs",
            "/swagger-ui"
    ));
}
