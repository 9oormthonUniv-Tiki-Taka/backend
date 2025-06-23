package com.tikitaka.api.dto.report;

import lombok.Data;

@Data
public class ReportRequest {
    private String targetType;
    private String targetId;
    private String reasonId;
    private String reason;
}
