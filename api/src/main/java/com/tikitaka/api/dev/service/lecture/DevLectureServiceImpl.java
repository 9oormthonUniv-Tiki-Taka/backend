package com.tikitaka.api.dev.service.lecture;

import com.tikitaka.api.dev.dto.LectureRequest;
import com.tikitaka.api.dev.dto.LectureResponse;
import com.tikitaka.api.dev.repository.DevLectureRepository;
import com.tikitaka.api.dev.repository.DevUserRepository;
import com.tikitaka.api.repository.RoomRepository;
import com.tikitaka.api.domain.lecture.Lecture;
import com.tikitaka.api.domain.room.Room;
import com.tikitaka.api.domain.user.User;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.util.List;
import java.util.stream.Collectors;

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
                .room(room)
                .dayOfWeek(DayOfWeek.of(request.getDayOfWeek() + 1))
                .user(user)
                .build();

        lectureRepository.save(lecture);
    }

    @Override
    public List<LectureResponse> getAllLectures() {
        return lectureRepository.findAll().stream()
                .map(lecture -> {
                    LectureResponse response = new LectureResponse();
                    response.setId(lecture.getLectureId());
                    response.setName(lecture.getName());
                    response.setDayOfWeek(lecture.getDayOfWeek().getValue() - 1);
                    response.setUserId(lecture.getUser().getId());
                    response.setRoomId(lecture.getRoom().getId());
                    return response;
                })
                .collect(Collectors.toList());
    }
}

