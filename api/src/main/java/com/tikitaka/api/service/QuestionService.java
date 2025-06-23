package com.tikitaka.api.service;

import com.tikitaka.api.dto.question.AiResponseDto;
import com.tikitaka.api.dto.question.QuestionDtos;
import com.tikitaka.api.dto.question.QuestionListDto;
import org.springframework.data.domain.Page;
import com.tikitaka.api.dto.question.QuestionBatchRequest;
import com.tikitaka.api.dto.question.QuestionBatchResponse;

import com.tikitaka.api.domain.user.UserRole;

public interface QuestionService {

    Page<QuestionListDto> getQuestions(Long lectureId, UserRole role, String status, String sort, int page);
    QuestionDtos.QuestionDetailResponse getQuestionDetail(Long lectureId, Long questionId, Long userId);
    void postComment(Long lectureId, Long questionId, Long userId, String content);
    void deleteComment(Long lectureId, Long questionId, Long commentId, Long userId);
    AiResponseDto getAIAnswer(Long lectureId, Long questionId);
    QuestionBatchResponse answerQuestionsBatch(Long lectureId, QuestionBatchRequest request);

}

