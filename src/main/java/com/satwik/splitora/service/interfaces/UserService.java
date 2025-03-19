package com.satwik.splitora.service.interfaces;

import com.satwik.splitora.persistence.dto.user.RegisterUserRequest;
import com.satwik.splitora.persistence.dto.user.UserDTO;

public interface UserService {
    String saveUser(RegisterUserRequest registerUserRequest);

    UserDTO findUser();

    String deleteUser();

    String updateUser(RegisterUserRequest registerUserRequest);

}
