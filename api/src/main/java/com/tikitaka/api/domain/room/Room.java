package com.tikitaka.api.domain.room;

import java.util.List;

import org.springframework.data.annotation.Id;

import com.tikitaka.api.domain.common.BaseTimeEntity;
import com.tikitaka.api.domain.lecture.Lecture;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.OneToMany;
import lombok.*;


@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Room extends BaseTimeEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    private String name;

    private String ip;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Lecture> lecture;
}
