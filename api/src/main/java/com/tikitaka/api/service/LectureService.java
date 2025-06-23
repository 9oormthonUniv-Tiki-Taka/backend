package com.tikitaka.api.service;

import com.tikitaka.api.domain.lecture.Lecture;
import java.util.List;

public interface LectureService {
    List<Lecture> getLecturesByUser(Long userId);
    List<?> getQuestions(Long lectureId);
}
