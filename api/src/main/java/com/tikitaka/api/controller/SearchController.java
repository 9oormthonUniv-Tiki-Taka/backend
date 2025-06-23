package com.tikitaka.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class SearchController {

    @PostMapping("/api/search")
    public ResponseEntity<?> search() {
        return ResponseEntity.ok("검색 완료");
    }
}

