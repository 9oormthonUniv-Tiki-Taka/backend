package com.tikitaka.api.dev.repository;

import com.tikitaka.api.domain.lecture.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DevLectureRepository extends JpaRepository<Lecture, Long> {
}
