package com.tikitaka.api.repository;

import com.tikitaka.api.domain.question.Question;
import com.tikitaka.api.domain.react.React;
import com.tikitaka.api.domain.react.ReactType;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ReactRepository extends JpaRepository<React, Long>{
    long countByTargetAndReactType(Question target, ReactType reactType);

    List<React> findByUserId(Long userId);

    List<React> findByUserIdAndType(Long userId, ReactType reactType);
}
