package com.tikitaka.api.repository;

import com.tikitaka.api.domain.comment.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import com.tikitaka.api.domain.question.Question;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByQuestionOrderByCreatedAtAsc(Question question);
    List<Comment> findAllByQuestionOrderByCreatedAtAsc(Question question);

}