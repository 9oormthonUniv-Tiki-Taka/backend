package com.tikitaka.api.dto.user;

import lombok.Data;
import java.time.LocalDateTime;

import org.springframework.data.domain.Page;

@Data
public class UserQuestionResponse {
    private Page<QuestionDto> questions;

    @Data
    public static class QuestionDto {
        private String id;
        private String lectureName;
        private String content;
        private String status;
        private LocalDateTime createdAt;
    }
}
