package com.tikitaka.api.controller;

import com.tikitaka.api.domain.user.UserRole;
import com.tikitaka.api.dto.question.*;
import com.tikitaka.api.jwt.CustomUserDetails;
import com.tikitaka.api.service.question.QuestionService;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/lectures/{lectureId}/questions")
@Tag(name = "Question API", description = "질문 관련 기능 (조회, 등록, 삭제 등)")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;

    @Operation(
            summary = "질문 목록 조회",
            description = "강의 ID에 해당하는 질문들을 조회합니다. 상태(status), 정렬기준(sort), 페이지(page)로 필터링할 수 있습니다."
    )
    @GetMapping
    public ResponseEntity<?> getQuestions(
            @PathVariable Long lectureId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false, defaultValue = "recent") String sort,
            @RequestParam(required = false, defaultValue = "0") int page,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Page<QuestionListDto> questions = questionService.getQuestions(lectureId, userDetails.getUser().getRole(), status, sort, page);
        return ResponseEntity.ok(questions);
    }

    @Operation(
            summary = "질문 상세 조회",
            description = "질문 ID에 해당하는 질문의 상세 정보를 조회합니다."
    )
    @GetMapping("/{questionId}")
    public ResponseEntity<?> getQuestionDetail(
            @PathVariable Long lectureId,
            @PathVariable Long questionId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        QuestionDtos.QuestionDetailResponse response = questionService.getQuestionDetail(lectureId, questionId, userDetails.getUser().getId());
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "질문에 댓글/답변 작성",
            description = "질문 ID에 댓글(또는 답변)을 등록합니다."
    )
    @PostMapping("/{questionId}/comments")
    public ResponseEntity<?> postComment(
            @PathVariable Long lectureId,
            @PathVariable Long questionId,
            @RequestBody CommentRequest request
    ) {
        questionService.postComment(lectureId, questionId, request.getUserId(), request.getContent());
        return ResponseEntity.ok("댓글/답변 등록 성공");
    }

    @Operation(
            summary = "댓글/답변 삭제",
            description = "질문 ID와 댓글 ID에 해당하는 댓글을 삭제합니다."
    )
    @DeleteMapping("/{questionId}/comments/{commentId}")
    public ResponseEntity<?> deleteComment(
            @PathVariable Long lectureId,
            @PathVariable Long questionId,
            @PathVariable Long commentId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        questionService.deleteComment(lectureId, questionId, commentId, userDetails.getUser().getId());
        return ResponseEntity.ok("댓글 삭제 성공");
    }

    @Operation(
            summary = "AI 답변 조회",
            description = "해당 질문에 대한 AI의 답변을 조회합니다."
    )
    @GetMapping("/{questionId}/ai")
    public ResponseEntity<AiResponseDto> getAIAnswer(
            @PathVariable Long lectureId,
            @PathVariable Long questionId
    ) {
        AiResponseDto response = questionService.getAIAnswer(lectureId, questionId);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "질문 일괄 응답 처리",
            description = "질문들에 대한 응답을 한 번에 처리합니다. (예: AI가 여러 질문에 답변)"
    )
    @PostMapping
    public ResponseEntity<QuestionBatchResponse> answerQuestionsBatch(
            @PathVariable Long lectureId,
            @RequestBody QuestionBatchRequest request
    ) {
        QuestionBatchResponse response = questionService.answerQuestionsBatch(lectureId, request);
        return ResponseEntity.ok(response);
    }
}


