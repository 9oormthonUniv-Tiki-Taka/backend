package com.tikitaka.api.repository;

import com.tikitaka.api.domain.point.PointTransaction;
import com.tikitaka.api.domain.point.PointType;


import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointTransactionRepository extends JpaRepository<PointTransaction, Long> {
    Long countByUserIdAndType(Long userId, PointType type);
    Page<PointTransaction> findByUserIdAndTypeOrderByCreatedAtDesc(Long userId, PointType type, Pageable pageable);
    Page<PointTransaction> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);
}
