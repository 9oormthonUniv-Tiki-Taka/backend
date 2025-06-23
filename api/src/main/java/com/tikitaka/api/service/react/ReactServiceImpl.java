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
        switch (request.getTargetType()) {
            case "like" -> {
                return handleLike(userId, request);
            }
            case "wonder" -> {
                return handleWonder(userId, request);

            }
            case "medal" -> {
                return handleMedal(userId, request);
            }
            default -> {
                throw new IllegalArgumentException("Invalid target type: " + request.getTargetType());
            }
        }
    }

    private ReactResponse handleLike(Long userId, ReactRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        
        Question question = questionRepository.findById(request.getTargetId())
                .orElseThrow(() -> new RuntimeException("Question not found with id: " + request.getTargetId()));
        

        React react = React.builder()
                .user(user)
                .target(question)
                .reactType(ReactType.LIKE)
                .build();
        
        reactRepository.save(react);
        Long count = reactRepository.countByTargetAndReactType(question, ReactType.LIKE);
        ReactResponse response = new ReactResponse();
        response.setTargetType("like");
        response.setTargetId(request.getTargetId());
        response.setReactCount(count);
        return response;
    }


    private ReactResponse handleWonder(Long userId, ReactRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        
        Question question = questionRepository.findById(request.getTargetId())
                .orElseThrow(() -> new RuntimeException("Question not found with id: " + request.getTargetId()));


        React react = React.builder()
                .user(user)
                .target(question)
                .reactType(ReactType.WONDER)
                .build();

        reactRepository.save(react);
        
        Long count = reactRepository.countByTargetAndReactType(question, ReactType.WONDER);

        ReactResponse response = new ReactResponse();
        response.setTargetType("wonder");
        response.setTargetId(request.getTargetId());
        response.setReactCount(count);
        return response;     
        
    }


    private ReactResponse handleMedal(Long userId, ReactRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));


        Question question = questionRepository.findById(request.getTargetId())
                .orElseThrow(() -> new RuntimeException("Question not found with id: " + request.getTargetId()));

        
        React react = React.builder()
                .user(user)
                .target(question)
                .reactType(ReactType.MEDAL)
                .build();

        reactRepository.save(react);
        
        Long count = reactRepository.countByTargetAndReactType(question, ReactType.MEDAL);

        ReactResponse response = new ReactResponse();
        response.setTargetType("medal");
        response.setTargetId(request.getTargetId());
        response.setReactCount(count);
        return response;
    }
    
}
