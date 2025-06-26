package com.tikitaka.api.dto.room;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import lombok.Data;

@Data
public class RoomStatusResponse {
    private String status; // live | idle
    private List<LectureDto> results;

    @Data
    public static class LectureDto {
        private Long id;
        private String name;
        private LocalTime startTime;
        private LocalTime endTime;
        private LocalDateTime createdAt;

        public static LectureDto fromEntity(com.tikitaka.api.domain.lecture.Lecture lecture) {
            LectureDto dto = new LectureDto();
            dto.setId(lecture.getLectureId());
            dto.setName(lecture.getName());
            dto.setStartTime(lecture.getStartTime());
            dto.setEndTime(lecture.getEndTime());
            dto.setCreatedAt(lecture.getCreatedAt());
            return dto;
        }
    }
}
