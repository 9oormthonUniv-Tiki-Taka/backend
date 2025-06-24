package com.tikitaka.api.repository;


import com.tikitaka.api.domain.report.Report;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReportRepository extends JpaRepository<Report, Long> {

    Page<Report> findByReporterId(Long userId, Pageable pageable);
    
}
