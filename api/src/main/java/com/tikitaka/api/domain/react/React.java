package com.tikitaka.api.domain.react;

import com.tikitaka.api.domain.common.BaseTimeEntity;
import com.tikitaka.api.domain.user.User;
import com.tikitaka.api.domain.question.Question;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class React extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reactId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id")
    private Question target;

    @Enumerated(EnumType.STRING)
    private ReactType reactType;
}
