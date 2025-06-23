package com.tikitaka.api.domain.question;

import com.tikitaka.api.domain.common.BaseTimeEntity;
import com.tikitaka.api.domain.user.User;
import com.tikitaka.api.domain.lecture.Lecture;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Question extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long questionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecture_id")
    private Lecture lecture;

    @Lob
    private String content;

    @Enumerated(EnumType.STRING)
    private QuestionStatus status;
}
