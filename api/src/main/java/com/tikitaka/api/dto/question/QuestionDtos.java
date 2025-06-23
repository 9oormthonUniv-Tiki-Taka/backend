package com.tikitaka.api.dto.question;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

public class QuestionDtos {

    @Data
    public static class UserDto {
        private String nickname;
        private String role;
        private String avatar;
    }

    @Data
    public static class UserSimpleDto {
        private String nickname;
        private String role;
    }

    @Data
    public static class AnswerDto {
        private String id;
        private UserSimpleDto user;
        private String content;
        private LocalDateTime createdAt;
    }

    @Data
    public static class QuestionDetailDto {
        private String id;
        private String content;
        private String status;
        private UserDto user;
        private List<AnswerDto> answer;
        private int answerCount;
        private LocalDateTime createdAt;
        private String medal;
        private int likes;
        private int wonder;
    }

    @Data
    public static class QuestionDetailResponse {
        private QuestionDetailDto question;
    }
}

