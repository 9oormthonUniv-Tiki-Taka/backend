package com.tikitaka.api.domain.lecture;

import java.time.DayOfWeek;
import java.time.LocalTime;

import com.tikitaka.api.domain.common.BaseTimeEntity;
import com.tikitaka.api.domain.user.User;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Lecture extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long lectureId;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "professor_id")
    private User professor;

    private DayOfWeek dayOfWeek;
    private LocalTime startTime;
}
