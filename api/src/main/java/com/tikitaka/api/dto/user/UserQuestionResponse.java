package com.tikitaka.api.dto.user;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class UserQuestionResponse {
    private List<QuestionDto> questions;

    @Data
    public static class QuestionDto {
        private String id;
        private String lectureName;
        private String content;
        private String status;
        private LocalDateTime createdAt;
    }
}
