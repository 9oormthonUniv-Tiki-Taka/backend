package com.tikitaka.api.dev.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.DayOfWeek;

@Data
@Schema(description = "강의 응답 DTO")
public class LectureResponse {

    private Long id;

    private String name;

    private int dayOfWeek;

    private Long userId;

    private Long roomId;
}

