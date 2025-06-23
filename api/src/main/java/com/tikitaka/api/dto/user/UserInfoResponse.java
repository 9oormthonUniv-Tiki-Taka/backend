package com.tikitaka.api.dto.user;

import lombok.Data;

@Data
public class UserInfoResponse {
    private UserDto user;

    @Data
    public static class UserDto {
        private String email;
        private String studentId;
        private Long point;
        private String name;
        private String avatar;
    }
}

