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
        private Long id;
        private UserSimpleDto user;
        private String content;
        private LocalDateTime createdAt;
    }

    @Data
    public static class QuestionDetailDto {
        private Long id;
        private String content;
        private String status;
        private UserDto user;
        private List<AnswerDto> answer;
        private Long answerCount;
        private LocalDateTime createdAt;
        private String medal;
        private Long likes;
        private boolean likedByCurrentUser;
        private Long wonder;
        private boolean wonderedByCurrentUser;
    }

    @Data
    public static class QuestionDetailResponse {
        private QuestionDetailDto question;
    }
}

