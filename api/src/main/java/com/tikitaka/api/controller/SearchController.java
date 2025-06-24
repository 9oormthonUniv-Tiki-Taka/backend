package com.tikitaka.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.web.bind.annotation.*;

import com.tikitaka.api.service.search.SearchService;

import lombok.RequiredArgsConstructor;

import com.tikitaka.api.dto.search.*;

@RestController
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @PostMapping("/api/search")
    public ResponseEntity<SearchResponse> search(@Header("user_id") Long userId, @RequestBody SearchRequest request) {
        SearchResponse response = searchService.search(userId, request);
        return ResponseEntity.ok(response);
    }
}

