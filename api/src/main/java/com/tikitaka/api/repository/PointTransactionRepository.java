package com.tikitaka.api.repository;

import com.tikitaka.api.domain.point.PointTransaction;
import com.tikitaka.api.domain.point.PointType;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PointTransactionRepository extends JpaRepository<PointTransaction, Long> {
    Long countByUserIdAndType(Long userId, PointType type);
    List<PointTransaction> findByUserIdAndTypeOrderByCreatedAtDesc(Long userId, PointType type);
    List<PointTransaction> findByUserIdOrderByCreatedAtDesc(Long userId);
}
