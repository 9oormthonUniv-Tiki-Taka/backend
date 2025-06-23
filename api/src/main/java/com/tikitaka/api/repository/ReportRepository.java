package com.tikitaka.api.repository;


import com.tikitaka.api.domain.report.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {
    
}
