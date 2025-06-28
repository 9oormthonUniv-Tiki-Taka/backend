package com.tikitaka.api.repository;

import com.tikitaka.api.domain.point.PointTransaction;
import com.tikitaka.api.domain.point.PointType;


import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface PointTransactionRepository extends JpaRepository<PointTransaction, Long> {
    @Query("SELECT COALESCE(SUM(p.amount), 0) FROM PointTransaction p WHERE p.user.id = :userId AND p.type = :type")
    Long sumByUserIdAndType(@Param("userId") Long userId, @Param("type") PointType type);

    Page<PointTransaction> findByUserIdAndTypeOrderByCreatedAtDesc(Long userId, PointType type, Pageable pageable);
    Page<PointTransaction> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);
}
