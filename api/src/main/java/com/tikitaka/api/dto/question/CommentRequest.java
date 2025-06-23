package com.tikitaka.api.dto.question;

import lombok.Data;

@Data
public class CommentRequest {
    private Long userId;    // 댓글 작성자 ID (학생 or 교수)
    private String content; // 댓글 내용
}
