package com.tikitaka.api.dev.dto;

import java.time.LocalDateTime;
import java.time.LocalTime;

import lombok.Data;

@Data
public class LectureDetailRequest {
    
    private Long roomId;
    private String name;
    private Long userId;
    private int dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalDateTime createdAt;
}
