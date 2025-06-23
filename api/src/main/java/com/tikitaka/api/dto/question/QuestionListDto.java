package com.tikitaka.api.dto.question;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class QuestionListDto {
    private String id;
    private String content;
    private String status;       // 교수용 (학생일 땐 null 가능)
    private LocalDateTime createdAt;
    private String medal;        // "gold" or null
}
