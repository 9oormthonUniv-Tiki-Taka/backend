package com.tikitaka.api.service;

import com.tikitaka.api.domain.question.Question;
import java.util.List;

public interface QuestionService {
    List<Question> getQuestions(Long lectureId);
    Question getQuestionDetail(Long lectureId, Long questionId);
    void registerComment(Long lectureId, Long questionId, Long userId);
    void deleteComment(Long lectureId, Long questionId, Long userId, Long commentId);
    String requestAiAnswer(Long lectureId, Long questionId, Long userId);
    void respondAllQuestions(Long lectureId, Long userId);
}
