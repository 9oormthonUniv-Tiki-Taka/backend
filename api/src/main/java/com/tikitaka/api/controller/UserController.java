package com.tikitaka.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.web.bind.annotation.*;

import com.tikitaka.api.dto.user.UserInfoResponse;
import com.tikitaka.api.dto.user.UserPointResponse;
import com.tikitaka.api.dto.user.UserQuestionResponse;
import com.tikitaka.api.dto.user.UserReactResponse;
import com.tikitaka.api.dto.user.UserReportResponse;
import com.tikitaka.api.service.user.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users/me")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<UserInfoResponse> getMe(@Header("userId") Long userId) {
        UserInfoResponse response = userService.getMyInfo(userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/points")
    public ResponseEntity<UserPointResponse> getPoints(@Header("userId") Long userId, @RequestParam(defaultValue = "all") String type) {
        UserPointResponse response = userService.getMyPoints(userId, type);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/questions")
    public ResponseEntity<UserQuestionResponse> getMyQuestions(@Header("userId") Long userId) {
        UserQuestionResponse response = userService.getMyQuestions(userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/reacts")
    public ResponseEntity<UserReactResponse> getMyReacts(@Header("userId") Long userId, @RequestParam(defaultValue = "like") String type) {
        UserReactResponse response = userService.getMyReacts(userId, type);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/reports")
    public ResponseEntity<UserReportResponse> getMyReports(@Header("userId") Long userId) {
        UserReportResponse response = userService.getMyReports(userId);
        return ResponseEntity.ok(response);
    }
}

