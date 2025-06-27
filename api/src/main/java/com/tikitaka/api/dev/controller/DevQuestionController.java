package com.tikitaka.api.dev.controller;

import com.tikitaka.api.dev.dto.QuestionRequest;
import com.tikitaka.api.dev.dto.QuestionResponse;
import com.tikitaka.api.dev.service.question.DevQuestionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Dev API", description = "개발용 API 모음")
@RestController
@RequestMapping("/api/dev/questions")
@RequiredArgsConstructor
public class DevQuestionController {
    private final DevQuestionService questionService;

    @Operation(summary = "질문 등록", description = "개발용 질문 등록 API입니다.")
    @PostMapping
    public ResponseEntity<Void> registerQuestion(@RequestBody QuestionRequest request) {
        questionService.registerQuestion(request);
        return ResponseEntity.ok().build();
    }
    @Operation(summary = "전체 질문 조회", description = "모든 질문 목록을 조회합니다.")
    @GetMapping
    public ResponseEntity<List<QuestionResponse>> getAllQuestions() {
        List<QuestionResponse> questions = questionService.getAllQuestions();
        return ResponseEntity.ok(questions);
    }
}

