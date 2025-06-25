package com.tikitaka.api.dev.dto;

import lombok.Data;

@Data
public class QuestionRequest {
    private Long userId;
    private Long lectureId;
    private String content;
}