package com.tikitaka.api.dev.controller;

import com.tikitaka.api.dev.dto.LectureRequest;
import com.tikitaka.api.dev.dto.LectureResponse;
import com.tikitaka.api.dev.service.lecture.DevLectureService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Dev API", description = "개발용 API 모음")
@RestController
@RequestMapping("/api/dev/lectures")
@RequiredArgsConstructor
public class DevLectureController {
    private final DevLectureService lectureService;

    @Operation(summary = "강의 등록", description = "개발용 강의 등록 API입니다.")
    @PostMapping
    public ResponseEntity<Void> registerLecture(@RequestBody LectureRequest request) {
        lectureService.registerLecture(request);
        return ResponseEntity.ok().build();
    }
    @Operation(summary = "전체 강의 조회", description = "모든 강의 목록을 조회합니다.")
    @GetMapping
    public ResponseEntity<List<LectureResponse>> getAllLectures() {
        List<LectureResponse> lectures = lectureService.getAllLectures();
        return ResponseEntity.ok(lectures);
    }
}

