package com.satwik.splitora.service.implementations;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.satwik.splitora.configuration.jwt.JwtUtil;
import com.satwik.splitora.constants.enums.RegistrationMethod;
import com.satwik.splitora.persistence.dto.user.AuthenticationResponse;
import com.satwik.splitora.persistence.entities.Group;
import com.satwik.splitora.persistence.entities.User;
import com.satwik.splitora.repository.GroupRepository;
import com.satwik.splitora.repository.UserRepository;
import com.satwik.splitora.service.interfaces.OAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.*;

@Service
public class OAuthServiceImpl implements OAuthService {
    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String CLIENT_ID;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String CLIENT_SECRET;

    @Value("${spring.security.oauth2.client.provider.google.token-uri}")
    private String TOKEN_URI;

    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String REDIRECT_URI;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    UserRepository userRepository;

    @Autowired
    GroupRepository groupRepository;


    @Override
    public AuthenticationResponse handleCallback(String code, String state) {
        Map<String, Object> postData = new HashMap<>();
        postData.put("code", code);
        postData.put("client_id", CLIENT_ID);
        postData.put("client_secret", CLIENT_SECRET);
        postData.put("redirect_uri", REDIRECT_URI);
        postData.put("grant_type", "authorization_code");

        // Convert Map to JSON string
        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody;
        try {
            requestBody = objectMapper.writeValueAsString(postData);
        } catch (Exception e) {
            System.out.println("Error converting map to JSON: " + e.getMessage());
            throw new RuntimeException(e.getMessage());
        }

        // Create headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Create an HttpEntity object containing the request body and headers
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

        // Create an instance of RestTemplate
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(TOKEN_URI, HttpMethod.POST, requestEntity, String.class);

        // getting token
        String json_response = response.getBody();
        ObjectMapper objectMapper1 = new ObjectMapper();
        JsonNode jsonNode;
        try {
            jsonNode = objectMapper1.readTree(json_response);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        String id_token = jsonNode.get("id_token").asText();

        HttpTransport transport = new NetHttpTransport();
        JsonFactory jsonFactory = new GsonFactory();
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                // Specify the CLIENT_ID of the app that accesses the backend:
                .setAudience(Collections.singletonList(CLIENT_ID))
                // Or, if multiple clients access the backend:
                //.setAudience(Arrays.asList(CLIENT_ID_1, CLIENT_ID_2, CLIENT_ID_3))
                .build();

        GoogleIdToken idToken = null;
        try {
            idToken = verifier.verify(id_token);
        } catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException(e);
        }

        assert idToken != null;
        GoogleIdToken.Payload payload = idToken.getPayload();

        String userId = payload.getSubject();
        System.out.println("User ID: " + userId);

        // Get profile information from payload
        String email = payload.getEmail();
        boolean emailVerified = payload.getEmailVerified();
        String name = (String) payload.get("name");
        String pictureUrl = (String) payload.get("picture");
        String locale = (String) payload.get("locale");
        String familyName = (String) payload.get("family_name");
        String givenName = (String) payload.get("given_name");

        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found!"));
        user.setUsername(UUID.randomUUID().toString());
        user.setEmail(email);
        user.setRegistrationMethod(RegistrationMethod.GOOGLE);
        user = userRepository.save(user);
        Group group = new Group();
        group.setGroupName("Non Grouped Expenses");
        group.setUser(user);
        group.setDefaultGroup(true);
        groupRepository.save(group);

        String token = jwtUtil.generateAccessToken(user);
        String refreshToken = jwtUtil.generateRefreshToken(user);

        return new AuthenticationResponse(token, refreshToken, "New token issue for google user.");
    }
}
