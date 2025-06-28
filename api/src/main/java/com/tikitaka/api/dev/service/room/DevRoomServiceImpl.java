package com.tikitaka.api.dev.service.room;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.tikitaka.api.dev.dto.RoomRequest;
import com.tikitaka.api.dev.dto.RoomResponse;
import com.tikitaka.api.dev.repository.DevRoomRepository;
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

    @Override
    public List<RoomResponse> getAllRooms() {
        return roomRepository.findAll().stream()
                .map(room -> {
                    RoomResponse response = new RoomResponse();
                    response.setId(room.getId());
                    response.setIp(room.getIp());
                    response.setName(room.getName());
                    return response;
                })
                .collect(Collectors.toList());
    }
}

