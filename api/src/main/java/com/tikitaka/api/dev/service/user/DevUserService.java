package com.tikitaka.api.dev.service.user;

import com.tikitaka.api.dev.dto.UserRequest;
import com.tikitaka.api.dev.dto.UserResponse;
import com.tikitaka.api.dev.dto.UserInfoResponse;

import java.util.List;

public interface DevUserService {
    UserResponse registerUser(UserRequest request);
    List<UserInfoResponse> getAllUsers();
}

