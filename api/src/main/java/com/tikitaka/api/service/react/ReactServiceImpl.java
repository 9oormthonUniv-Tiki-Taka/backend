package com.tikitaka.api.service.react;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tikitaka.api.domain.question.Question;
import com.tikitaka.api.domain.react.React;
import com.tikitaka.api.domain.react.ReactType;
import com.tikitaka.api.domain.user.User;
import com.tikitaka.api.dto.react.*;
import com.tikitaka.api.repository.QuestionRepository;
import com.tikitaka.api.repository.ReactRepository;
import com.tikitaka.api.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReactServiceImpl implements ReactService {

    private final ReactRepository reactRepository;
    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public ReactResponse reactToContent(Long userId, ReactRequest request) {
        return switch (request.getTargetType()) {
            case "like" -> toggleReact(userId, request, ReactType.LIKE);
            case "wonder" -> toggleReact(userId, request, ReactType.WONDER);
            case "medal" -> toggleReact(userId, request, ReactType.MEDAL);
            default -> throw new IllegalArgumentException("Invalid target type: " + request.getTargetType());
        };
    }

    private ReactResponse toggleReact(Long userId, ReactRequest request, ReactType type) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        Question question = questionRepository.findById(request.getTargetId())
                .orElseThrow(() -> new RuntimeException("Question not found with id: " + request.getTargetId()));

        // 기존 리액션 여부 확인
        React existingReact = reactRepository
                .findByUserAndTargetAndType(user, question, type)
                .orElse(null);

        boolean reacted;

        if (existingReact != null) {
            // 이미 눌렀으면 취소
            reactRepository.delete(existingReact);
            reacted = false;
        } else {
            // 안 눌렀으면 등록
            React newReact = React.builder()
                    .user(user)
                    .target(question)
                    .type(type)
                    .build();
            reactRepository.save(newReact);
            reacted = true;
        }

        // 현재 리액션 개수 조회
        Long count = reactRepository.countByTargetAndType(question, type);

        // 응답 생성
        ReactResponse response = new ReactResponse();
        response.setTargetType(type.name().toLowerCase());
        response.setTargetId(request.getTargetId());
        response.setReactCount(count);
        response.setReacted(reacted); // ✅ 새 필드: 현재 유저가 리액션 눌렀는지 여부
        return response;
    }
}

