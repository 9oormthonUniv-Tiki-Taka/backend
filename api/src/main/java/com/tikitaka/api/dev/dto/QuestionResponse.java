package com.tikitaka.api.dev.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "질문 응답 DTO")
public class QuestionResponse {
    private Long id;

    private String content;

    private String status;

    private Long lectureId;
}
