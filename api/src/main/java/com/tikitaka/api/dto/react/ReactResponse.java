package com.tikitaka.api.dto.react;

import lombok.Data;

@Data
public class ReactResponse {
    private String targetType; // like | wonder | medal
    private String targetId;
    private Integer reactCount;
}

