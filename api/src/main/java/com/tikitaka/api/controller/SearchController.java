package com.tikitaka.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.tikitaka.api.service.search.SearchService;
import com.tikitaka.api.dto.search.*;
import com.tikitaka.api.jwt.CustomUserDetails;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Tag(name = "Search API", description = "AI 기반 질문 유사도 검색 API")
public class SearchController {

    private final SearchService searchService;

    @Operation(
            summary = "유사 질문 검색",
            description = "사용자가 입력한 질문과 유사한 기존 질문을 검색합니다."
    )
    @PostMapping("/api/search")
    public ResponseEntity<SearchResponse> search(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody SearchRequest request
    ) {
        SearchResponse response = searchService.search(userDetails.getUser().getId(), request);
        return ResponseEntity.ok(response);
    }
}


