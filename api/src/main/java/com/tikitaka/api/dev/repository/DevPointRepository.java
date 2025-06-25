package com.tikitaka.api.dev.repository;

import com.tikitaka.api.domain.point.PointTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DevPointRepository extends JpaRepository<PointTransaction, Long> {
}
