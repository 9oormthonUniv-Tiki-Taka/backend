package com.tikitaka.api.service;

import com.tikitaka.api.domain.react.ReactType;

public interface ReactService {
    int reactToContent(Long userId, Long targetId, ReactType reactType);
}
