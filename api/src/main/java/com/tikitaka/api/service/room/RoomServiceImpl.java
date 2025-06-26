package com.tikitaka.api.service.room;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tikitaka.api.domain.lecture.Lecture;
import com.tikitaka.api.domain.room.Room;
import com.tikitaka.api.dto.room.RoomStatusResponse;
import com.tikitaka.api.repository.LectureRepository;
import com.tikitaka.api.repository.RoomRepository;
import com.tikitaka.api.service.room.RoomService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final LectureRepository lectureRepository;

    @Override
    public RoomStatusResponse getRoomStatus(Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Room not found. id=" + roomId));

        LocalDateTime now = LocalDateTime.now();
        DayOfWeek today = now.getDayOfWeek();
        LocalTime currentTime = now.toLocalTime();

        List<Lecture> todayLectures = lectureRepository.findByRoom_IdAndDayOfWeek(roomId, today);

        // 현재 진행중인 강의
        Optional<Lecture> currentLecture = todayLectures.stream()
                .filter(l -> !l.getStartTime().isAfter(currentTime) && l.getEndTime().isAfter(currentTime))
                .findFirst();

        if (currentLecture.isPresent()) {
            return buildResponse("live", List.of(RoomStatusResponse.LectureDto.fromEntity(currentLecture.get())));
        }

        // 다음 강의 (startTime 이후인 것 중 가장 빠른 것)
        Optional<Lecture> nextLecture = todayLectures.stream()
                .filter(l -> l.getStartTime().isAfter(currentTime))
                .sorted(Comparator.comparing(Lecture::getStartTime))
                .findFirst();

        if (nextLecture.isPresent()) {
            return buildResponse("idle", List.of(RoomStatusResponse.LectureDto.fromEntity(nextLecture.get())));
        }

        // 더 이상 강의 없음
        return buildResponse("idle", List.of());
    }

    private RoomStatusResponse buildResponse(String status, List<RoomStatusResponse.LectureDto> results) {
        RoomStatusResponse response = new RoomStatusResponse();
        response.setStatus(status);
        response.setResults(results);
        return response;
    }
}

