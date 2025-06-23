package com.tikitaka.api.dto.lecture;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class LectureDto {
    private String id;
    private String name;
    private String room;
    private LocalDateTime createdAt;
    private Integer frequency; // 교수 전용 (학생일 경우 null 가능)
}
