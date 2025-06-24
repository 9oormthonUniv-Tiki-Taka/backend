package com.tikitaka.api.controller;

import com.tikitaka.api.domain.user.UserRole;
import com.tikitaka.api.dto.question.*;
import com.tikitaka.api.service.QuestionService;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/lectures/{lectureId}/questions")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;

    @GetMapping
    public ResponseEntity<?> getQuestions(
            @PathVariable Long lectureId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false, defaultValue = "recent") String sort,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestHeader("X-User-Role") String userRoleStr
    ) {
        // UserRole enum 변환
        UserRole role = UserRole.valueOf(userRoleStr.toUpperCase());

        Page<QuestionListDto> questions = questionService.getQuestions(lectureId, role, status, sort, page);
        return ResponseEntity.ok(questions);
    }

    @GetMapping("/{questionId}")
    public ResponseEntity<?> getQuestionDetail(
            @PathVariable Long lectureId,
            @PathVariable Long questionId,
            @RequestParam Long userId  // 임시로 사용자 ID 직접 전달
    ) {
        QuestionDtos.QuestionDetailResponse response = questionService.getQuestionDetail(lectureId, questionId, userId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{questionId}/comments")
    public ResponseEntity<?> postComment(
            @PathVariable Long lectureId,
            @PathVariable Long questionId,
            @RequestBody CommentRequest request
    ) {
        questionService.postComment(lectureId, questionId, request.getUserId(), request.getContent());
        return ResponseEntity.ok("댓글/답변 등록 성공");
    }

    @DeleteMapping("/{questionId}/comments/{commentId}")
    public ResponseEntity<?> deleteComment(
            @PathVariable Long lectureId,
            @PathVariable Long questionId,
            @PathVariable Long commentId,
            @RequestParam Long userId  // 삭제 요청자 ID (추후 인증 토큰으로 대체 가능)
    ) {
        questionService.deleteComment(lectureId, questionId, commentId, userId);
        return ResponseEntity.ok("댓글 삭제 성공");
    }

    @GetMapping("/{questionId}/ai")
    public ResponseEntity<AiResponseDto> getAIAnswer(
            @PathVariable Long lectureId,
            @PathVariable Long questionId
    ) {
        AiResponseDto response = questionService.getAIAnswer(lectureId, questionId);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<QuestionBatchResponse> answerQuestionsBatch(
            @PathVariable Long lectureId,
            @RequestBody QuestionBatchRequest request
    ) {
        QuestionBatchResponse response = questionService.answerQuestionsBatch(lectureId, request);
        return ResponseEntity.ok(response);
    }
}

