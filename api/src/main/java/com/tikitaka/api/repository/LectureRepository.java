package com.tikitaka.api.repository;

import com.tikitaka.api.domain.lecture.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LectureRepository extends JpaRepository<Lecture, Long> {
    List<Lecture> findByNameContainingIgnoreCase(String name);
}
