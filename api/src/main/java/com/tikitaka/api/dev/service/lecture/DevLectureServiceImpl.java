package com.tikitaka.api.dev.service;

import com.tikitaka.api.dev.dto.LectureRequest;
import com.tikitaka.api.domain.lecture.Lecture;
import com.tikitaka.api.domain.user.User;
import com.tikitaka.api.dev.repository.DevLectureRepository;
import com.tikitaka.api.dev.repository.DevUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DevLectureServiceImpl implements DevLectureService {
    private final DevLectureRepository lectureRepository;
    private final DevUserRepository userRepository;

    @Override
    public void registerLecture(LectureRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Lecture lecture = Lecture.builder()
                .name(request.getName())
                .room(request.getRoom())
                .dayOfWeek(request.getDayOfWeek())
                .startTime(request.getStartTime())
                .user(user)
                .build();

        lectureRepository.save(lecture);
    }
}
