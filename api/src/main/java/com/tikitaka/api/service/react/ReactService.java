package com.tikitaka.api.service.react;

import com.tikitaka.api.dto.react.ReactRequest;
import com.tikitaka.api.dto.react.ReactResponse;

public interface ReactService {
    ReactResponse reactToContent(Long userId, ReactRequest request);
}
