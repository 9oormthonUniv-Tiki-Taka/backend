package com.tikitaka.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/lectures")
public class LectureController {

    @GetMapping
    public ResponseEntity<?> getLectureList() {
        return ResponseEntity.ok("수업 목록 조회");
    }

    @GetMapping("/{lectureId}/live/questions")
    public ResponseEntity<?> getLiveQuestions(@PathVariable Long lectureId) {
        return ResponseEntity.ok("실시간 대화 내역 조회");
    }
}