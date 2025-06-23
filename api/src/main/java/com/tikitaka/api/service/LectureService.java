package com.tikitaka.api.service;

import com.tikitaka.api.domain.lecture.Lecture;
import com.tikitaka.api.dto.lecture.LectureDto;

import java.util.List;

public interface LectureService {
    List<LectureDto> getLecturesByUser(Long userId,String filter);
    List<?> getQuestions(Long lectureId);
}
