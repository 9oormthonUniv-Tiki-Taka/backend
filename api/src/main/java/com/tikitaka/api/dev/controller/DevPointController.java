package com.tikitaka.api.dev.controller;

import com.tikitaka.api.dev.dto.PointRequest;
import com.tikitaka.api.dev.service.point.DevPointService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Dev API", description = "개발용 API 모음")
@RestController
@RequestMapping("/api/dev/points")
@RequiredArgsConstructor
public class DevPointController {
    private final DevPointService pointService;

    @Operation(summary = "포인트 등록", description = "개발용 포인트 등록 API입니다.")
    @PostMapping
    public ResponseEntity<Void> registerPoint(@RequestBody PointRequest request) {
        pointService.registerPoint(request);
        return ResponseEntity.ok().build();
    }
}

