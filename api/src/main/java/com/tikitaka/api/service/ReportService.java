package com.tikitaka.api.service;

public interface ReportService {
    void reportContent(Long userId, Long contentId, String reportType, String reason);
}
