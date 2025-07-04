package com.tikitaka.api.dev.controller;

import com.tikitaka.api.dev.dto.UserRequest;
import com.tikitaka.api.dev.dto.UserResponse;
import com.tikitaka.api.dev.dto.UserInfoResponse;  // 추가
import com.tikitaka.api.dev.service.user.DevUserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Dev API", description = "개발용 API 모음")
@RestController
@RequestMapping("/api/dev/users")
@RequiredArgsConstructor
public class DevUserController {
    private final DevUserService userService;

    @Operation(summary = "유저 등록", description = "개발용 유저 등록 API입니다.")
    @PostMapping
    public ResponseEntity<UserResponse> registerUser(@RequestBody UserRequest request) {
        UserResponse response = userService.registerUser(request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "전체 유저 조회", description = "개발용 전체 유저 목록을 조회합니다.")
    @GetMapping
    public ResponseEntity<List<UserInfoResponse>> getAllUsers() {
        List<UserInfoResponse> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }
}



