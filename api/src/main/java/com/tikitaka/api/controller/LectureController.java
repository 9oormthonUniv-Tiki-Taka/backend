package com.tikitaka.api.controller;

import com.tikitaka.api.domain.user.User;
import com.tikitaka.api.dto.lecture.LectureListResponse;
import com.tikitaka.api.dto.question.QuestionDtos.QuestionDetailDto;
import com.tikitaka.api.service.lecture.LectureService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/lectures")
@Tag(name = "Lecture API", description = "강의 정보 및 실시간 질문 조회 API")
public class LectureController {

    private final LectureService lectureService;

    @Operation(
            summary = "강의 목록 조회",
            description = "로그인한 사용자의 역할(학생/교수)에 따라 수강 또는 담당 중인 강의 목록을 조회합니다."
    )
    @GetMapping
    public LectureListResponse getLectureList(
            @AuthenticationPrincipal User user,
            @RequestParam(defaultValue = "recent") String sort) {
        return lectureService.getLectureList(user, sort);
    }

    @Operation(
            summary = "실시간 질문 목록 조회",
            description = "특정 강의의 실시간 질문 및 답변 목록을 조회합니다."
    )
    @GetMapping("/{lectureId}/live/questions")
    public Map<String, List<QuestionDetailDto>> getLiveQuestions(@PathVariable Long lectureId) {
        List<QuestionDetailDto> questions = lectureService.getLiveQuestions(lectureId);
        return Map.of("questions", questions);
    }
}
