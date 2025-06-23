package com.tikitaka.api.repository;

import com.tikitaka.api.domain.lecture.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LectureRepository extends JpaRepository<Lecture, Long> {
    
}
