package com.tikitaka.api.dev.dto;

import lombok.Data;

import java.time.LocalDateTime;


@Data
public class LectureRequest {
    private Long roomId;
    private String name;
    private Long userId;
    private int dayOfWeek;
    private LocalDateTime createdAt;
}

