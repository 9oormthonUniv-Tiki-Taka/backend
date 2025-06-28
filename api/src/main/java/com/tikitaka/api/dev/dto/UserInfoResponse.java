package com.tikitaka.api.dev.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UserInfoResponse {

    private Long id;

    private String email;

    private String name;

    private String role;

    private String studentId;

    private String avatarUrl;

    private String sub;
}
