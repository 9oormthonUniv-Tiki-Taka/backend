package com.tikitaka.api.dev.service.lecture;

import com.tikitaka.api.dev.dto.LectureRequest;
import com.tikitaka.api.domain.lecture.Lecture;
import com.tikitaka.api.domain.room.Room;
import com.tikitaka.api.domain.user.User;
import com.tikitaka.api.dev.repository.DevLectureRepository;
import com.tikitaka.api.dev.repository.DevUserRepository;
import com.tikitaka.api.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.time.DayOfWeek;

@Service
@RequiredArgsConstructor
public class DevLectureServiceImpl implements DevLectureService {
    private final DevLectureRepository lectureRepository;
    private final DevUserRepository userRepository;
    private final RoomRepository roomRepository;

    @Override
    public void registerLecture(LectureRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new RuntimeException("Room not found"));

        Lecture lecture = Lecture.builder()
                .name(request.getName())
                .room(room)  // 여기서 room 할당
                .dayOfWeek(DayOfWeek.of(request.getDayOfWeek() + 1))
                .user(user)
                .build();

        lectureRepository.save(lecture);
    }
}
