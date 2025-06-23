package com.tikitaka.api.dto.react;

import com.tikitaka.api.domain.react.ReactType;
import lombok.Data;

@Data
public class ReactRequest {
    private ReactType reactType; // LIKE, WONDER, MEDAL
    private Long targetId;       // Question id
}


