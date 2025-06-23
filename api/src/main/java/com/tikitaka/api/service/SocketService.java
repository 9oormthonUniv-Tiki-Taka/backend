package com.tikitaka.api.service;

public interface SocketService {
    void handleLiveSocket(Long lectureId, Object payload);
}
