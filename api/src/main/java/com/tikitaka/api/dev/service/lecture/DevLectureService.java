package com.tikitaka.api.dev.service.lecture;

import com.tikitaka.api.dev.dto.LectureRequest;
import com.tikitaka.api.dev.dto.LectureResponse;

import java.util.List;

public interface DevLectureService {
    void registerLecture(LectureRequest request);
    List<LectureResponse> getAllLectures();
}

