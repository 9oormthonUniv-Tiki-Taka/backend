package com.tikitaka.api.service;

import com.tikitaka.api.domain.lecture.Lecture;
import com.tikitaka.api.domain.user.UserRole;
import com.tikitaka.api.dto.lecture.LectureDto;
import com.tikitaka.api.dto.question.QuestionDtos;

import java.util.List;

public interface LectureService {
    List<LectureDto> getLectureList(UserRole role, String sort);
    List<QuestionDtos.QuestionDetailDto> getLiveQuestions(Long lectureId);

}
