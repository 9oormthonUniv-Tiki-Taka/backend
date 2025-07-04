package com.tikitaka.api.dto.user;

import lombok.Data;
import java.time.LocalDateTime;

import org.springframework.data.domain.Page;

@Data
public class UserReportResponse {
    private Page<ReportDto> report;

    @Data
    public static class ReportDto {
        private String id;
        private String type; // question | comment
        private String status; // 신고완료 | 처리완료 | 신고반려 | 신고취소
        private String reason;
        private LocalDateTime createdAt;
    }
}
