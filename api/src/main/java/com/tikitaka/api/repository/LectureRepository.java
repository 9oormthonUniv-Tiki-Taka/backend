package com.tikitaka.api.repository;

import com.tikitaka.api.domain.lecture.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.DayOfWeek;
import java.util.List;

import java.util.List;

public interface LectureRepository extends JpaRepository<Lecture, Long> {
    List<Lecture> findByNameContainingIgnoreCase(String name);

    @Query("SELECT ul.lecture FROM UserLecture ul WHERE ul.user.userId = :userId")
    List<Lecture> findLecturesByUserId(@Param("userId") Long userId);

    @Query("SELECT l FROM Lecture l WHERE (:dayOfWeek IS NULL OR l.dayOfWeek = :dayOfWeek)")
    List<Lecture> findAllByDayOfWeek(@Param("dayOfWeek") DayOfWeek dayOfWeek);
}
