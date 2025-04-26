package com.satwik.splitora.controller;

import com.satwik.splitora.configuration.security.LoggedInUser;
import com.satwik.splitora.persistence.dto.ResponseModel;
import com.satwik.splitora.persistence.dto.user.RegisterUserRequest;
import com.satwik.splitora.persistence.dto.user.UserDTO;
import com.satwik.splitora.service.interfaces.UserService;
import com.satwik.splitora.util.ResponseUtil;
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
     * @param registerUserRequest the request object containing user registration details.
     * @return a ResponseEntity containing a ResponseModel with the result of the registration process.
     */
    @PostMapping("/register")
    public ResponseEntity<ResponseModel<String>> registerUser(@Valid @RequestBody RegisterUserRequest registerUserRequest) {
        log.info("Post Endpoint: register user with request: {}", registerUserRequest);
        String response = userService.saveUser(registerUserRequest);
        ResponseModel<String> responseModel = ResponseUtil.success(response, HttpStatus.OK, "User registered successfully");
        log.info("Post Endpoint: register user with response: {}", responseModel);
        return ResponseEntity.status(HttpStatus.OK).body(responseModel);
    }

    /**
     * Retrieves the current user details.
     *
     * @return a ResponseEntity containing a ResponseModel with the UserDTO of the currently logged-in user.
     */
    @GetMapping("")
    public ResponseEntity<ResponseModel<UserDTO>> getUser() {
        log.info("Get Endpoint: get user");
        UserDTO response = userService.findUser();
        ResponseModel<UserDTO> responseModel = ResponseUtil.success(response, HttpStatus.OK, "User details retrieved successfully");
        log.info("Get Endpoint: get user with response: {}", responseModel);
        return ResponseEntity.status(HttpStatus.OK).body(responseModel);
    }

    /**
     * Updates the details of a user.
     *
     * @param updateUserRequest the request object containing updated user details.
     * @return a ResponseEntity containing a ResponseModel with the result of the update process.
     */
    @PutMapping("update/{userId}")
    public ResponseEntity<ResponseModel<String>> updateUser(@Valid @RequestBody RegisterUserRequest updateUserRequest) {
        log.info("Put Endpoint: update user with id: {}, and register user request: {}", loggedInUser.getUserId(), updateUserRequest);
        String response = userService.updateUser(updateUserRequest);
        ResponseModel<String> responseModel = ResponseUtil.success(response, HttpStatus.OK, "User updated successfully");
        log.info("Put Endpoint: update user with response: {}", responseModel);
        return ResponseEntity.status(HttpStatus.OK).body(responseModel);
    }

    /**
     * Deletes the current user.
     *
     * @return a ResponseEntity containing a ResponseModel with the result of the deletion process.
     */
    @DeleteMapping("delete")
    public ResponseEntity<ResponseModel<String>> deleteUser() {
        log.info("Delete Endpoint: delete user");
        String response = userService.deleteUser();
        ResponseModel<String> responseModel = ResponseUtil.success(response, HttpStatus.OK, "User deleted successfully");
        log.info("Delete Endpoint: delete user with response: {}", responseModel);
        return ResponseEntity.status(HttpStatus.OK).body(responseModel);
    }
}