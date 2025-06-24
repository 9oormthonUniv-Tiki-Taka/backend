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
    private String room;
    private String name;
    private String room;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private DayOfWeek dayOfWeek;
    private LocalTime startTime;
}
