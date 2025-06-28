package com.tikitaka.api.domain.user;

import com.tikitaka.api.domain.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(nullable = false, unique = true)
    private String email;

    private String name;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    private String studentId;

    private String avatarUrl;
    private String sub;
}