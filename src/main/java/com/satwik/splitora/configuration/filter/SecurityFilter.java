package com.satwik.splitora.configuration.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.satwik.splitora.configuration.jwt.JwtUtil;
import com.satwik.splitora.configuration.security.LoggedInUser;
import com.satwik.splitora.constants.SecurityConstants;
import com.satwik.splitora.constants.enums.ErrorCode;
import com.satwik.splitora.constants.enums.UserRole;
import com.satwik.splitora.persistence.dto.ErrorDetails;
import com.satwik.splitora.persistence.dto.ErrorResponseModel;
import com.satwik.splitora.repository.UserRepository;
import com.satwik.splitora.util.ResponseUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;

@Slf4j
@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private LoggedInUser loggedInUser;

    private boolean isWhitelisted(String url) {
        return SecurityConstants.WHITELISTED_URLS.stream().anyMatch(url::contains);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestURI = request.getRequestURI();

        // Check if the request URI is whitelisted
        if (isWhitelisted(requestURI)) {
            filterChain.doFilter(request, response);
            return;
        }

        // read the token from the header
        String token = request.getHeader("Authorization");

        if (token == null || !token.startsWith("Bearer ")) {
            log.error("Missing or invalid Authorization header");
            sendErrorResponse(response, "Missing or invalid Authorization header", new ErrorDetails(
                    ErrorCode.MISSING_REQUIRED_FIELD.getCode(),
                    MessageFormat.format(ErrorCode.MISSING_REQUIRED_FIELD.getMessage(), "Authorization header")
            ));
            return;
        }

        token = token.substring(7);
        try {

            // get the user email using the token
            String userEmail = jwtUtil.getUserEmail(token);
            // get the user id using the token
            UUID userId = UUID.fromString(jwtUtil.getUserId(token));
            // get the user role using the token
            String userRole = jwtUtil.getUserRole(token);

            // username should not be empty, cont-auth must be empty
            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                // get the user details
                UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);

                // validate token
                boolean isValid = jwtUtil.validateToken(token, userDetails);

                if (isValid) {

                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userEmail, null, Collections.singletonList(new SimpleGrantedAuthority(userRole)));

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authToken);

                    loggedInUser.setUserEmail(userEmail);
                    loggedInUser.setUserId(userId);
                    loggedInUser.setRole(UserRole.fromString(userRole));
                }
            }
        } catch (Exception e) {
            log.error("Access Token validation failed: {}", e.getMessage());
            sendErrorResponse(response, "Access Token validation failed", new ErrorDetails(
                    ErrorCode.ACCESS_TOKEN_INVALID.getCode(),
                    ErrorCode.ACCESS_TOKEN_INVALID.getMessage()
            ));
            return;
        }

        filterChain.doFilter(request, response);
    }

    private void sendErrorResponse(HttpServletResponse response, String message, ErrorDetails errorDetails) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        ErrorResponseModel errorResponseModel = ResponseUtil.error(
                message,
                HttpStatus.UNAUTHORIZED,
                errorDetails
        );

        ObjectMapper  mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.writeValue(response.getWriter(), errorResponseModel);
    }
}
