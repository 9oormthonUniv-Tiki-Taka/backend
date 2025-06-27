package com.tikitaka.api.repository;


import com.tikitaka.api.domain.report.Report;


import com.tikitaka.api.domain.report.ReportType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.tikitaka.api.domain.user.User;


public interface ReportRepository extends JpaRepository<Report, Long> {

    Page<Report> findByReporterId(Long userId, Pageable pageable);
    boolean existsByReporterAndTargetTypeAndTargetId(User reporter, ReportType targetType, Long targetId);


}
