package com.tikitaka.api.service;

import com.tikitaka.api.domain.user.User;
import com.tikitaka.api.dto.lecture.LectureListResponse;
import com.tikitaka.api.dto.question.QuestionDtos;

import java.util.List;

public interface LectureService {
    LectureListResponse getLectureList(User user, String sort);

    List<QuestionDtos.QuestionDetailDto> getLiveQuestions(Long lectureId);
}
