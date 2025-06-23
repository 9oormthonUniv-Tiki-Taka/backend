package com.tikitaka.api.service;

public interface ReactService {
    void reactToContent(Long userId, Long contentId, String reactionType);
}
