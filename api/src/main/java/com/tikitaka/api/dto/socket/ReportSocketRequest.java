package com.tikitaka.api.dto.socket;

import lombok.Data;

@Data
public class ReportSocketRequest {
    private Long targetId;
    private String reason;
}
