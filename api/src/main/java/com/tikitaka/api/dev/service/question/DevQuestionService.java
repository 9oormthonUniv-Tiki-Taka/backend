package com.tikitaka.api.dev.service.question;

import com.tikitaka.api.dev.dto.QuestionRequest;
import com.tikitaka.api.dev.dto.QuestionResponse;

import java.util.List;

public interface DevQuestionService {
    void registerQuestion(QuestionRequest request);
    List<QuestionResponse> getAllQuestions();
}
