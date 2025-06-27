package com.tikitaka.api.config;

import static org.mockito.Mockito.mock;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import com.tikitaka.api.jwt.JwtTokenProvider;
import com.tikitaka.api.repository.CommentRepository;
import com.tikitaka.api.repository.LectureRepository;
import com.tikitaka.api.repository.PointTransactionRepository;
import com.tikitaka.api.repository.QuestionRepository;
import com.tikitaka.api.repository.ReactRepository;
import com.tikitaka.api.repository.ReportRepository;
import com.tikitaka.api.repository.RoomRepository;
import com.tikitaka.api.repository.UserRepository;

@TestConfiguration
public class TestConfig {
    
    @Bean
    public RoomRepository roomRepository() {
        return mock(RoomRepository.class);
    }

    @Bean
    public UserRepository userRepository() {
        return mock(UserRepository.class);
    }

    @Bean
    public JwtTokenProvider jwtTokenProvider() {
        return mock(JwtTokenProvider.class);
    }

    @Bean
    public CommentRepository commentRepository() {
        return mock(CommentRepository.class);
    }

    @Bean
    public LectureRepository lectureRepository() {
        return mock(LectureRepository.class);
    }

    @Bean
    public PointTransactionRepository pointTransactionRepository() {
        return mock(PointTransactionRepository.class);
    }

    @Bean
    public QuestionRepository questionRepository() {
        return mock(QuestionRepository.class);
    }

    @Bean
    public ReactRepository reactRepository() {
        return mock(ReactRepository.class);
    }

    @Bean
    public ReportRepository reportRepository() {
        return mock(ReportRepository.class);
    }
}
