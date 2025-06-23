package com.tikitaka.api.dto.react;

import lombok.Data;

@Data
public class ReactRequest {
    private String targetType; // like | wonder | medal
    private Long targetId;
}

