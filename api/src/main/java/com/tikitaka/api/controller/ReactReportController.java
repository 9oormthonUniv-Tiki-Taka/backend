package com.tikitaka.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ReactReportController {

    @PostMapping("/api/reacts")
    public ResponseEntity<?> postReact() {
        return ResponseEntity.ok("반응 등록 완료");
    }

    @PostMapping("/api/reports")
    public ResponseEntity<?> postReport() {
        return ResponseEntity.ok("신고 등록 완료");
    }
}

