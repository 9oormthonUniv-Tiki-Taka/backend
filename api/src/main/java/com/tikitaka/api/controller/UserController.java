package com.tikitaka.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users/me")
public class UserController {

    @GetMapping
    public ResponseEntity<?> getMe() {
        return ResponseEntity.ok("내 정보 조회");
    }

    @GetMapping("/points")
    public ResponseEntity<?> getPoints() {
        return ResponseEntity.ok("포인트 내역 조회");
    }

    @GetMapping("/questions")
    public ResponseEntity<?> getMyQuestions() {
        return ResponseEntity.ok("내가 작성한 질문");
    }

    @GetMapping("/reacts")
    public ResponseEntity<?> getMyReacts() {
        return ResponseEntity.ok("내 반응 조회");
    }

    @GetMapping("/reports")
    public ResponseEntity<?> getMyReports() {
        return ResponseEntity.ok("내 신고 내역 조회");
    }
}

