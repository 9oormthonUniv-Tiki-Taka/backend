package com.tikitaka.api.service.room;

import com.tikitaka.api.dto.room.RoomStatusResponse;

public interface RoomService {
    RoomStatusResponse getRoomStatus(Long roomId);
}

