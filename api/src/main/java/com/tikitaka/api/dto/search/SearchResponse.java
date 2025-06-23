package com.tikitaka.api.dto.search;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class SearchResponse {
    private String type;
    private List<Object> results;

    @Data
    @AllArgsConstructor
    public static class QuestionResultForProfessor {
        private String id;
        private String content;
        private String status;
        private LocalDateTime createdAt;
        private String medal;
    }

    @Data
    @AllArgsConstructor
    public static class QuestionResultForStudent {
        private String id;
        private String content;
        private LocalDateTime createdAt;
        private String medal;
    }

    @Data
    @AllArgsConstructor
    public static class LectureResultForProfessor {
        private String id;
        private String name;
        private String room;
        private String frequency;
        private LocalDateTime createdAt;
    }

    @Data
    @AllArgsConstructor
    public static class LectureResultForStudent {
        private String id;
        private String name;
        private String room;
        private LocalDateTime createdAt;
    }
}
