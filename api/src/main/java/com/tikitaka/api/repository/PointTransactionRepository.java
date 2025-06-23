package com.tikitaka.api.repository;

import com.tikitaka.api.domain.point.PointTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointTransactionRepository extends JpaRepository<PointTransaction, Long> {
    
}
