package com.tikitaka.api.dto.react;

import com.tikitaka.api.domain.react.ReactType;
import lombok.Data;

@Data
public class ReactResponse {
    private ReactType reactType;
    private Long targetId;
    private Integer reactCount;
}


