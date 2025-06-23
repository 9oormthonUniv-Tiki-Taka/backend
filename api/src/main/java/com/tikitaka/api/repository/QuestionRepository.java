package com.tikitaka.api.repository;

import com.tikitaka.api.domain.question.Question;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByContentContainingIgnoreCase(String content);
}
