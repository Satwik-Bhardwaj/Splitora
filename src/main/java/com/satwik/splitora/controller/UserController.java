package com.satwik.splitora.controller;

import com.satwik.splitora.configuration.security.LoggedInUser;
import com.satwik.splitora.persistence.dto.user.RegisterUserRequest;
import com.satwik.splitora.persistence.dto.user.UserDTO;
import com.satwik.splitora.service.interfaces.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private LoggedInUser loggedInUser;

    /**
     * Registers a new user.
     *
     * This endpoint processes the request to register a new user with the provided registration details.
     * It logs the incoming request and the resulting response.
     *
     * @param registerUserRequest the request object containing user registration details.
     * @return a ResponseEntity containing a string response message indicating the result of the registration process.
     */
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody RegisterUserRequest registerUserRequest) {
        log.info("Post Endpoint: register user with request: {}", registerUserRequest);
        String response = userService.saveUser(registerUserRequest);
        log.info("Post Endpoint: register user with response: {}", response);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * Retrieves the current user details.
     *
     * This endpoint processes the request to retrieve the details of the currently logged-in user.
     * It logs the incoming request and the resulting response.
     *
     * @return a ResponseEntity containing the UserDTO of the currently logged-in user.
     */
    @GetMapping("")
    public ResponseEntity<UserDTO> getUser() {
        log.info("Get Endpoint: get user");
        UserDTO response = userService.findUser();
        log.info("Get Endpoint: get user with response: {}", response);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * Updates the details of a user.
     *
     * This endpoint processes the request to update the details of a user identified by the given user ID
     * with the provided registration details. It logs the incoming request and the resulting response.
     *
     * @param updateUserRequest the request object containing updated user details.
     * @return a ResponseEntity containing a string response message indicating the result of the update process.
     */
    @PutMapping("update/{userId}")
    public ResponseEntity<String> updateUser(@Valid @RequestBody RegisterUserRequest updateUserRequest) {
        log.info("Put Endpoint: update user with id: {}, and register user request: {}", loggedInUser.getUserId(), updateUserRequest);
        String response = userService.updateUser(updateUserRequest);
        log.info("Put Endpoint: update user with response: {}", response);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * Deletes the current user.
     *
     * This endpoint processes the request to delete the currently logged-in user. It logs the incoming request
     * and the resulting response.
     *
     * @return a ResponseEntity containing a string response message indicating the result of the deletion process.
     */
    @DeleteMapping("delete")
    public ResponseEntity<String> deleteUser() {
        log.info("Delete Endpoint: delete user");
        String response = userService.deleteUser();
        log.info("Delete Endpoint: delete user with response: {}", response);
        return ResponseEntity.ok(response);
    }

}
