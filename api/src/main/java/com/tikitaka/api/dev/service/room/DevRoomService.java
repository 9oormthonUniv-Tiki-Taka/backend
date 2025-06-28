package com.tikitaka.api.dev.service.room;

import com.tikitaka.api.dev.dto.RoomRequest;
import com.tikitaka.api.dev.dto.RoomResponse;

import java.util.List;

public interface DevRoomService {
    void registerRoom(RoomRequest request);
    List<RoomResponse> getAllRooms();
}
