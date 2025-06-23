package com.tikitaka.api.service;

import com.tikitaka.api.domain.react.React;
import com.tikitaka.api.domain.react.ReactType;
import com.tikitaka.api.domain.question.Question;
import com.tikitaka.api.domain.user.User;
import com.tikitaka.api.repository.QuestionRepository;
import com.tikitaka.api.repository.ReactRepository;
import com.tikitaka.api.repository.UserRepository;
import com.tikitaka.api.service.ReactService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReactServiceImpl implements ReactService {

    private final ReactRepository reactRepository;
    private final UserRepository userRepository;
    private final QuestionRepository questionRepository;

    @Override
    @Transactional
    public int reactToContent(Long userId, Long targetId, ReactType reactType) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Question question = questionRepository.findById(targetId)
                .orElseThrow(() -> new IllegalArgumentException("Question not found"));

        React react = React.builder()
                .user(user)
                .target(question)
                .reactType(reactType)
                .build();
        reactRepository.save(react);

        return reactRepository.countByTargetAndReactType(question, reactType);
    }
}

