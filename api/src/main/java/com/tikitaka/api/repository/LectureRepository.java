package com.tikitaka.api.repository;

import com.tikitaka.api.domain.lecture.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.tikitaka.api.domain.user.User;
import org.springframework.data.domain.Sort;


import java.time.DayOfWeek;
import java.util.List;

public interface LectureRepository extends JpaRepository<Lecture, Long> {
    List<Lecture> findByNameContainingIgnoreCase(String name);

    List<Lecture> findByUser(User user, Sort sort);

    @Query("SELECT l FROM Lecture l WHERE (:dayOfWeek IS NULL OR l.dayOfWeek = :dayOfWeek)")
    List<Lecture> findAllByDayOfWeek(@Param("dayOfWeek") DayOfWeek dayOfWeek);

    List<Lecture> findByRoom_IdAndDayOfWeek(Long roomId, DayOfWeek dayOfWeek);
}
