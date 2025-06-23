package com.tikitaka.api.service.socket;

import com.tikitaka.api.dto.socket.LiveSocketRequest;

public interface SocketService {
    void handleLiveSocket(Long lectureId, LiveSocketRequest payload, Long userId);
}


