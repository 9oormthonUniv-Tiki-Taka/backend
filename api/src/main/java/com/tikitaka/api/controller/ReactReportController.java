package com.tikitaka.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.web.bind.annotation.*;

import com.tikitaka.api.dto.react.*;
import com.tikitaka.api.dto.report.*;
import com.tikitaka.api.service.react.ReactService;
import com.tikitaka.api.service.report.ReportService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ReactReportController {

    private final ReportService reportService;
    private final ReactService reactService;

    @PostMapping("/api/reacts")
    public ResponseEntity<ReactResponse> postReact(@Header("userId") Long userId, ReactRequest request) {
        ReactResponse response = reactService.reactToContent(userId, request); // 예시로 userId 1L 사용
        return ResponseEntity.ok(response);
    }

    @PostMapping("/api/reports")
    public ResponseEntity<?> postReport(@Header("userId") Long userId, @RequestBody ReportRequest request) {
        reportService.reportContent(userId, request);
        return ResponseEntity.ok("신고 등록 완료");
    }
}

