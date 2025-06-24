package com.tikitaka.api.dto.report;

import lombok.Data;

@Data
public class ReportRequest {
    private String targetType;
    private Long targetId;
    private Long reasonId;
    private String reason;
}
