package com.tikitaka.api.repository;

import com.tikitaka.api.domain.question.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    @Query("SELECT q FROM Question q JOIN FETCH q.user WHERE q.lecture.lectureId = :lectureId ORDER BY q.createdAt ASC")
    List<Question> findByLectureId(@Param("lectureId") Long lectureId);

    @Query("SELECT q.lecture.lectureId, COUNT(q) FROM Question q WHERE q.lecture.lectureId IN :lectureIds GROUP BY q.lecture.lectureId")
    List<Object[]> countQuestionsByLectureIds(@Param("lectureIds") List<Long> lectureIds);
}

