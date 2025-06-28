package com.tikitaka.api.repository;

import com.tikitaka.api.domain.question.Question;
import com.tikitaka.api.domain.react.React;
import com.tikitaka.api.domain.react.ReactType;
import com.tikitaka.api.domain.user.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReactRepository extends JpaRepository<React, Long>{
    Long countByTargetAndType(Question target, ReactType reactType);

    Page<React> findByUserId(Long userId, Pageable pageable);

    Page<React> findByUserIdAndType(Long userId, ReactType reactType, Pageable pageable);

    Optional<React> findByUserAndTargetAndType(User user, Question target, ReactType type);

}
