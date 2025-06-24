package com.tikitaka.api.controller;

import com.tikitaka.api.domain.user.User;
import com.tikitaka.api.dto.lecture.LectureListResponse;
import com.tikitaka.api.dto.question.QuestionDtos;
import com.tikitaka.api.service.LectureService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/lectures")
public class LectureController {
    private final LectureService lectureService;

    @GetMapping
    public LectureListResponse getLectureList(
            @AuthenticationPrincipal User user,
            @RequestParam(defaultValue = "recent") String sort) {

        return lectureService.getLectureList(user, sort);
    }

    @GetMapping("/{lectureId}/live/questions")
    public ResponseEntity<?> getLiveQuestions(@PathVariable Long lectureId) {
        List<QuestionDtos.QuestionDetailDto> questions = lectureService.getLiveQuestions(lectureId);
        return ResponseEntity.ok(Map.of("questions", questions));
    }
}