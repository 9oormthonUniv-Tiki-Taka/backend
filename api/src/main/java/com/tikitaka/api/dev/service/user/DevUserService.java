package com.tikitaka.api.dev.service.user;

import com.tikitaka.api.dev.dto.UserRequest;
import com.tikitaka.api.dev.dto.UserResponse;

public interface DevUserService {
    UserResponse registerUser(UserRequest request);
}

