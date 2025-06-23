package com.tikitaka.api.dto.search;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class SearchResponse {
    private String type; // "question" or "lecture"
    private List<?> results;
}

