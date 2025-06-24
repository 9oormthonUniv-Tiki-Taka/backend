package com.tikitaka.api.service.socket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tikitaka.api.dto.socket.AnswerSocketRequest;
import com.tikitaka.api.dto.socket.AnsweredSocketRequest;
import com.tikitaka.api.dto.socket.LiveSocketRequest;
import com.tikitaka.api.dto.socket.QuestionSocketRequest;
import com.tikitaka.api.dto.socket.ReactSocketRequest;
import com.tikitaka.api.repository.LectureRepository;
import com.tikitaka.api.repository.QuestionRepository;
import com.tikitaka.api.repository.UserRepository;
import com.tikitaka.api.repository.CommentRepository;
import com.tikitaka.api.repository.ReactRepository;

import com.tikitaka.api.domain.user.User;
import com.tikitaka.api.domain.lecture.Lecture;
import com.tikitaka.api.domain.react.*;
import com.tikitaka.api.domain.question.*;
import com.tikitaka.api.domain.comment.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SocketServiceImpl implements SocketService {
    private final UserRepository userRepository;
    private final LectureRepository lectureRepository;
    private final QuestionRepository questionRepository;
    private final CommentRepository commentRepository;
    private final ReactRepository reactRepository;

    @Override
    @Transactional
    public void handleLiveSocket(Long lectureId, LiveSocketRequest message, Long userId) {
        switch (message.getType()) {
            case "question" -> {
                QuestionSocketRequest req = convert(message, QuestionSocketRequest.class);
                handleQuestion(req, userId, lectureId);
            }
            case "answer" -> {
                AnswerSocketRequest req = convert(message, AnswerSocketRequest.class);
                handleAnswer(req, userId);
            }
            case "like", "wonder", "medal" -> {
                ReactSocketRequest req = convert(message, ReactSocketRequest.class);
                handleReaction(req, message.getType(), userId);
            }
            case "answered" -> {
                AnsweredSocketRequest req = convert(message, AnsweredSocketRequest.class);
                handleAnswered(req);
            }
        }
    }

    private <T> T convert(LiveSocketRequest message, Class<T> targetType) {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.convertValue(message.getRequest(), targetType);
    }

    private void handleQuestion(QuestionSocketRequest request, Long userId, Long lectureId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        
        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new RuntimeException("Lecture not found with id: " + lectureId));

        Question question = Question.builder()
                .user(user)
                .lecture(lecture)
                .content(request.getContent())
                .status(QuestionStatus.WAITING)
                .build();
        questionRepository.save(question);
    }

    private void handleAnswer(AnswerSocketRequest request, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        
        Question question = questionRepository.findById(request.getQuestionId())
                .orElseThrow(() -> new RuntimeException("Question not found with id: " + request.getQuestionId()));
        
        Comment comment = Comment.builder()
                .responder(user)
                .question(question)
                .content(request.getContent())
                .question(question)
                .build();
        
        commentRepository.save(comment);
    }

    private void handleReaction(ReactSocketRequest requestReactSocketRequest, String type, Long userId) {
        switch (type) {
            case "like" -> handleLike(requestReactSocketRequest, userId);
            case "wonder" -> handleWonder(requestReactSocketRequest, userId);
            case "medal" -> handleMedal(requestReactSocketRequest, userId);
        }
    }

    private void handleAnswered(AnsweredSocketRequest request) {
        Question question = questionRepository.findById(request.getQuestionId())
                .orElseThrow(() -> new RuntimeException("Question not found with id: " + request.getQuestionId()));
        
        question.updateStatus(QuestionStatus.ANSWERED);
    }

    private void handleLike(ReactSocketRequest request, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        
        Question question = questionRepository.findById(request.getQuestionId())
                .orElseThrow(() -> new RuntimeException("Question not found with id: " + userId));

        React react = React.builder()
                .user(user)
                .target(question)
                .type(ReactType.LIKE)
                .build();
        
        reactRepository.save(react);       
    }

    private void handleWonder(ReactSocketRequest request, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        
        Question question = questionRepository.findById(request.getQuestionId())
                .orElseThrow(() -> new RuntimeException("Question not found with id: " + userId));

        React react = React.builder()
                .user(user)
                .target(question)
                .type(ReactType.WONDER)
                .build();
        
        reactRepository.save(react);       
    }

    private void handleMedal(ReactSocketRequest request, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        
        Question question = questionRepository.findById(request.getQuestionId())
                .orElseThrow(() -> new RuntimeException("Question not found with id: " + userId));

        React react = React.builder()
                .user(user)
                .target(question)
                .type(ReactType.MEDAL)
                .build();
        
        reactRepository.save(react);      
    }
}