package com.tikitaka.api.dev.service.room;

import java.util.List;

import org.springframework.stereotype.Service;

import com.tikitaka.api.dev.dto.RoomRequest;
import com.tikitaka.api.dev.repository.DevLectureRepository;
import com.tikitaka.api.dev.repository.DevRoomRepository;
import com.tikitaka.api.domain.lecture.Lecture;
import com.tikitaka.api.domain.room.Room;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class DevRoomServiceImpl implements DevRoomService {

    private final DevRoomRepository roomRepository;

    @Override
    public void registerRoom(RoomRequest request) {
        Room room = Room.builder()
                .ip(request.getIp())
                .name(request.getName())
                .build();
        roomRepository.save(room);
    }
}
