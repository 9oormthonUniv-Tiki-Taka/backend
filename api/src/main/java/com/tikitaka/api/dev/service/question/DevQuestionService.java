package com.tikitaka.api.dev.service;

import com.tikitaka.api.dev.dto.QuestionRequest;

public interface DevQuestionService {
    void registerQuestion(QuestionRequest request);
}
