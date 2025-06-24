package com.tikitaka.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.web.bind.annotation.*;

import com.tikitaka.api.dto.react.*;
import com.tikitaka.api.dto.report.*;
import com.tikitaka.api.service.react.ReactService;
import com.tikitaka.api.service.report.ReportService;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Tag(name = "Reaction & Report API", description = "질문에 대한 리액션 및 신고 관련 API")
public class ReactReportController {

    private final ReportService reportService;
    private final ReactService reactService;

    @Operation(
            summary = "리액션 등록",
            description = "질문 또는 댓글에 대해 LIKE, WONDER, MEDAL 등의 리액션을 등록합니다."
    )
    @PostMapping("/api/reacts")
    public ResponseEntity<ReactResponse> postReact(
            @Header("userId") Long userId,
            @RequestBody ReactRequest request
    ) {
        ReactResponse response = reactService.reactToContent(userId, request);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "신고 등록",
            description = "질문 또는 댓글을 부적절한 콘텐츠로 신고합니다."
    )
    @PostMapping("/api/reports")
    public ResponseEntity<?> postReport(
            @Header("userId") Long userId,
            @RequestBody ReportRequest request
    ) {
        reportService.reportContent(userId, request);
        return ResponseEntity.ok("신고 등록 완료");
    }
}


