package com.tikitaka.api.controller;

import com.tikitaka.api.domain.user.UserRole;
import com.tikitaka.api.dto.lecture.LectureDto;
import com.tikitaka.api.dto.lecture.LectureListResponse;
import com.tikitaka.api.dto.question.QuestionDtos;
import com.tikitaka.api.service.LectureService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/lectures")
public class LectureController {
    private final LectureService lectureService;

    @GetMapping
    public ResponseEntity<?> getLectureList(
            @RequestParam(value = "sort", defaultValue = "recent") String sort,
            @AuthenticationPrincipal User user
    ) {
        List<LectureDto> lectures = lectureService.getLectureList(user.getRole(), sort);
        return ResponseEntity.ok(new LectureListResponse(lectures));
    }

    @GetMapping("/{lectureId}/live/questions")
    public ResponseEntity<?> getLiveQuestions(@PathVariable Long lectureId) {
        List<QuestionDtos.QuestionDetailDto> questions = lectureService.getLiveQuestions(lectureId);
        return ResponseEntity.ok(Map.of("questions", questions));
    }
}