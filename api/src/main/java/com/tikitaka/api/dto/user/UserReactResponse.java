package com.tikitaka.api.dto.user;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class UserReactResponse {
    private List<ReactDto> reacts;

    @Data
    public static class ReactDto {
        private String id;
        private String type; // like | wonder
        private String lectureName;
        private String content;
        private LocalDateTime createdAt;
    }
}
