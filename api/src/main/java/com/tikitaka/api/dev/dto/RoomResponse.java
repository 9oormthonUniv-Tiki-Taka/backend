package com.tikitaka.api.dev.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "강의실 정보 응답 DTO")
public class RoomResponse {
    private Long id;

    private String ip;

    private String name;
}

