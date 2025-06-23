package com.tikitaka.api.service.search;

import com.tikitaka.api.dto.search.SearchRequest;
import com.tikitaka.api.dto.search.SearchResponse;

import com.tikitaka.api.repository.LectureRepository;
import com.tikitaka.api.repository.QuestionRepository;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {
    private final LectureRepository lectureRepository;
    private final QuestionRepository questionRepository;

    @Override
    public SearchResponse search(SearchRequest request) {
        if (request.getType() == "question") {
            return searchQuestions(request.getQuery());
        } else if (request.getType() == "lecture") {
            return searchLectures(request.getQuery());
        } else {
            throw new IllegalArgumentException("Invalid search type: " + request.getType());
        }
    }

    private SearchResponse searchQuestions(String query) {

        SearchResponse response = new SearchResponse();
        response.setType("question");
        response.setResults(questionRepository.findByContentContainingIgnoreCase(query));
        return response;
    }

    private SearchResponse searchLectures(String query) {
        SearchResponse response = new SearchResponse();
        response.setType("lecture");
        response.setResults(lectureRepository.findByNameContainingIgnoreCase(query));
        return response;
    }
}

