package com.satwik.splitora.configuration.jwt;

import com.satwik.splitora.exception.AccessTokenInvalidException;
import com.satwik.splitora.exception.RefreshTokenInvalidException;
import com.satwik.splitora.persistence.entities.User;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {

    private static final Logger log = LoggerFactory.getLogger(JwtUtil.class);
    // secret key for access token
    @Value("${jwt.access.secretKey}")
    private String ACCESS_SECRET_KEY;
    // secret key for refresh token
    @Value("${jwt.refresh.secretKey}")
    private String REFRESH_SECRET_KEY;
    // expiration time for access token
    @Value("${jwt.access.expirationTimeInMinutes}")
    private long ACCESS_TOKEN_EXP_TIME;
    // expiration time for refresh token
    @Value("${jwt.refresh.expirationTimeInMinutes}")
    private long REFRESH_TOKEN_EXP_TIME;

    // generate access token method
    public String generateAccessToken(User user) {
        Map <String, Object> extraClaims = new HashMap<>();
        extraClaims.put("role", "REGULAR_USER");
        return buildToken(user, extraClaims, ACCESS_SECRET_KEY, ACCESS_TOKEN_EXP_TIME);
    }

    public String buildToken(User user, Map<String, Object> extraClaims, String secretKey, Long expirationTime) {
        Date issuedAt = new Date(System.currentTimeMillis());
        extraClaims.put("userId", user.getId());
        return Jwts.builder()
                .setSubject(user.getEmail())
                .setIssuedAt(issuedAt)
                .addClaims(extraClaims)
                .setIssuer("com.splitora.app")
                .setExpiration(new Date((expirationTime * 60 * 1000) + issuedAt.getTime()))
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    // generate refresh token method
    public String generateRefreshToken(User user) {
        return buildToken(user, new HashMap<>(), REFRESH_SECRET_KEY, REFRESH_TOKEN_EXP_TIME);
    }

    // get claims
    private Claims getClaims(String token, String secretKey) {
            return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    }
    public Claims getClaimsOfAccessToken(String accessToken) {
        try {
            return getClaims(accessToken, ACCESS_SECRET_KEY);
        }catch (ExpiredJwtException expiredJwtException) {
            throw new AccessTokenInvalidException("Access token is expired! Use refresh token to get new access token");
        }catch (SignatureException signatureException) {
            throw new AccessTokenInvalidException("Access token has invalid signature!");
        }
    }

    public Claims getClaimsOfRefreshToken(String refreshToken) {
        try {
            return getClaims(refreshToken, REFRESH_SECRET_KEY);
        }catch (ExpiredJwtException expiredJwtException) {
            throw new RefreshTokenInvalidException("Refresh token is expired! Please log in again...");
        }catch (SignatureException signatureException) {
            throw new RefreshTokenInvalidException("Refresh token has invalid signature!");
        }
    }

    // get user id method
    public String getUserEmail(String token) {
        return getClaimsOfAccessToken(token).getSubject();
    }

    // get expiration date of token
    private Date getExpirationDate(String token) {
        return getClaimsOfAccessToken(token).getExpiration();
    }

    // validate expiration date
    private boolean isTokenExp(String token) {
        Date expDate = getExpirationDate(token);
        return expDate.before(new Date(System.currentTimeMillis()));
    }

    // validate token itself
    public boolean validateToken(String token, UserDetails userDetails) {

        String email = getUserEmail(token);
        return email != null && email.equals(userDetails.getUsername()) && !isTokenExp(token);
    }
}
