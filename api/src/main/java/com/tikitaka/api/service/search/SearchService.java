package com.tikitaka.api.service.search;

import com.tikitaka.api.dto.search.SearchRequest;
import com.tikitaka.api.dto.search.SearchResponse;

public interface SearchService {
    SearchResponse search(SearchRequest request);
}
