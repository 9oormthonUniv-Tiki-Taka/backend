package com.tikitaka.api.dev.dto;

import com.tikitaka.api.domain.point.PointType;
import lombok.Data;

@Data
public class PointRequest {
    private Long userId;
    private Integer amount;
    private String reason;
    private PointType type;
}
