package com.tikitaka.api.controller;

import com.tikitaka.api.domain.user.User;
import com.tikitaka.api.dto.lecture.LectureListResponse;
import com.tikitaka.api.dto.question.QuestionDtos.QuestionDetailDto;
import com.tikitaka.api.service.lecture.LectureService;
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
    public Map<String, List<QuestionDetailDto>> getLiveQuestions(@PathVariable Long lectureId) {
        List<QuestionDetailDto> questions = lectureService.getLiveQuestions(lectureId);
        return Map.of("questions", questions);
    }
}