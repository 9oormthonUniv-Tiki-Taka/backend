package com.tikitaka.api.dto.user;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class UserPointResponse {
    private Long point;
    private List<PointDto> points;

    @Data
    public static class PointDto {
        private String id;
        private Integer amount;
        private String type; // "earn" | "spend"
        private String reason;
        private LocalDateTime createdAt;
    }
}

