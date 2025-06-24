package com.tikitaka.api.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.web.bind.annotation.*;

import com.tikitaka.api.dto.user.*;
import com.tikitaka.api.service.user.UserService;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users/me")
@RequiredArgsConstructor
@Tag(name = "User API", description = "내 정보, 활동 내역, 포인트 등을 조회하는 API")
public class UserController {

    private final UserService userService;

    @Operation(
            summary = "내 정보 조회",
            description = "현재 로그인한 사용자의 기본 정보를 조회합니다."
    )
    @GetMapping
    public ResponseEntity<UserInfoResponse> getMe(@Header("userId") Long userId) {
        UserInfoResponse response = userService.getMyInfo(userId);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "내 포인트 내역 조회",
            description = "내 포인트 적립/차감 내역을 유형별로 페이징 조회합니다. (예: all, gain, use)"
    )
    @GetMapping("/points")
    public ResponseEntity<UserPointResponse> getPoints(@Header("userId") Long userId,
                                                       @RequestParam(defaultValue = "all") String type,
                                                       @RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("created_at").descending());
        UserPointResponse response = userService.getMyPoints(userId, type, pageable);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "내 질문 내역 조회",
            description = "내가 작성한 질문 내역을 페이징하여 조회합니다."
    )
    @GetMapping("/questions")
    public ResponseEntity<UserQuestionResponse> getMyQuestions(@Header("userId") Long userId,
                                                               @RequestParam(defaultValue = "0") int page,
                                                               @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("created_at").descending());
        UserQuestionResponse response = userService.getMyQuestions(userId, pageable);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "내 리액션 내역 조회",
            description = "내가 누른 리액션(WONDER, LIKE, MEDAL 등)을 유형별로 조회합니다."
    )
    @GetMapping("/reacts")
    public ResponseEntity<UserReactResponse> getMyReacts(@Header("userId") Long userId,
                                                         @RequestParam(defaultValue = "like") String type,
                                                         @RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("created_at").descending());
        UserReactResponse response = userService.getMyReacts(userId, type, pageable);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "내 신고 내역 조회",
            description = "내가 신고한 질문/댓글 내역을 페이징하여 조회합니다."
    )
    @GetMapping("/reports")
    public ResponseEntity<UserReportResponse> getMyReports(@Header("userId") Long userId,
                                                           @RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("created_at").descending());
        UserReportResponse response = userService.getMyReports(userId, pageable);
        return ResponseEntity.ok(response);
    }
}

