package com.satwik.splitora.configuration.filter;

import com.satwik.splitora.configuration.jwt.JwtUtil;
import com.satwik.splitora.configuration.security.LoggedInUser;
import com.satwik.splitora.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.*;

@Slf4j
@Component
public class SecurityFilter extends OncePerRequestFilter {

    private static final List<String> WHITELISTED_URLS = new ArrayList<>(Arrays.asList(
            "/api/v1/auth/login",
            "/api/v1/auth/refresh_token",
            "/api/v1/user/register",
            "/api/v1/oauth2/login",
            "/api/v1/oauth2/callback",
            "/api/v1/auth/getUser"

    )); // Add your whitelisted URLs here

    @Autowired
    UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private LoggedInUser loggedInUser;

    private boolean isWhitelisted(String url) {
        return WHITELISTED_URLS.stream().anyMatch(url::contains);
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

        if(token != null) {
            token = token.substring(7);

            // get the user email using the token
            String userEmail = jwtUtil.getUserEmail(token);

            // username should not be empty, cont-auth must be empty
            if(userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null)  {

                // get the user details
                UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);

                // validate token
                boolean isValid = jwtUtil.validateToken(token, userDetails.getUsername());

                if(isValid) {
                    loggedInUser.setUserEmail(userEmail);

                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userEmail, userDetails.getPassword(), userDetails.getAuthorities());

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }

            }
        }

        filterChain.doFilter(request, response);
    }
}
