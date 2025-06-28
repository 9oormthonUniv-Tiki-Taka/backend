package com.tikitaka.api.dev.dto;

import com.tikitaka.api.domain.user.UserRole;
import lombok.Data;

@Data
public class UserRequest {
    private String email;
    private String name;
    private UserRole role;
    private String studentId;
    private String avatarUrl;
    private String sub;
}


