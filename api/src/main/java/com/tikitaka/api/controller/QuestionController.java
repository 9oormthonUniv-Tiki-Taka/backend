package com.tikitaka.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/lectures/{lectureId}/questions")
public class QuestionController {

    @GetMapping
    public ResponseEntity<?> getQuestions(@PathVariable Long lectureId) {
        return ResponseEntity.ok("질문 목록 조회");
    }

    @GetMapping("/{questionId}")
    public ResponseEntity<?> getQuestionDetail(@PathVariable Long lectureId, @PathVariable Long questionId) {
        return ResponseEntity.ok("질문 상세 조회");
    }

    @PostMapping("/{questionId}/comments")
    public ResponseEntity<?> postComment(@PathVariable Long lectureId, @PathVariable Long questionId) {
        return ResponseEntity.ok("댓글/답변 등록");
    }

    @DeleteMapping("/{questionId}/comments/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Long lectureId, @PathVariable Long questionId, @PathVariable Long commentId) {
        return ResponseEntity.ok("댓글 삭제");
    }

    @GetMapping("/{questionId}/ai")
    public ResponseEntity<?> getAIAnswer(@PathVariable Long lectureId, @PathVariable Long questionId) {
        return ResponseEntity.ok("AI 응답 요청");
    }

    @PostMapping
    public ResponseEntity<?> answerQuestionsBatch(@PathVariable Long lectureId) {
        return ResponseEntity.ok("질문 일괄 응답");
    }
}

