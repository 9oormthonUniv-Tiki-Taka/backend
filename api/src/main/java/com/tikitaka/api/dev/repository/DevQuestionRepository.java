package com.tikitaka.api.dev.repository;

import com.tikitaka.api.domain.question.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DevQuestionRepository extends JpaRepository<Question, Long> {
}
