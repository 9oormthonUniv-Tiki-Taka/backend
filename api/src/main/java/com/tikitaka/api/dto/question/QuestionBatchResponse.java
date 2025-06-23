package com.tikitaka.api.dto.question;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class QuestionBatchResponse {
    private List<QuestionBatchDto> questions;

    @Data
    public static class QuestionBatchDto {
        private String id;
        private String status; // waiting | answered
        private String content;
        private LocalDateTime createdAt;
    }
}
