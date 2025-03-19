package com.satwik.splitora.service.implementations;

import com.satwik.splitora.configuration.security.LoggedInUser;
import com.satwik.splitora.persistence.entities.User;
import com.satwik.splitora.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService {

    @Autowired
    LoggedInUser loggedInUser;

    @Autowired
    UserRepository userRepository;


    public User getAuthorizedUser() {
        return userRepository.findByEmail(loggedInUser.getUserEmail()).orElseThrow(() -> new RuntimeException("User not found"));
    }
}
