package com.tikitaka.api.dto.search;

import lombok.Data;

@Data
public class SearchRequest {
    private String type;  // "question" | "lecture"
    private String query;
}
