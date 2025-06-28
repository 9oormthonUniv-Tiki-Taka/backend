package com.tikitaka.api.repository;

import com.tikitaka.api.domain.lecture.Lecture;
import com.tikitaka.api.domain.question.Question;

import java.util.List;

import com.tikitaka.api.domain.question.QuestionStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByContentContainingIgnoreCase(String content);
    Page<Question> findByUserId(Long userId, Pageable pageable);
    Long countByLecture(Lecture lecture);

    List<Question> findByLecture_LectureIdOrderByCreatedAtDesc(Long lectureId);

    @Query("""
        SELECT q FROM Question q
        WHERE q.lecture.lectureId = :lectureId
        AND (:status IS NULL OR q.status = :status)
        """)
    Page<Question> findByLectureIdAndStatus(
            @Param("lectureId") Long lectureId,
            @Param("status") QuestionStatus status,
            Pageable pageable
    );

    @Query("SELECT q FROM Question q JOIN FETCH q.user WHERE q.questionId = :questionId AND q.lecture.lectureId = :lectureId")
    Optional<Question> findByLectureIdAndQuestionIdWithUser(@Param("lectureId") Long lectureId, @Param("questionId") Long questionId);
}

