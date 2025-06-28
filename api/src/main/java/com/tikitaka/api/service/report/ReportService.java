package com.tikitaka.api.service.report;

import com.tikitaka.api.dto.report.ReportRequest;

public interface ReportService {
    void reportContent(Long userId, ReportRequest request);
}
