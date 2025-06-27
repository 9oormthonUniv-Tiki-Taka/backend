package com.tikitaka.api.service.socket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tikitaka.api.dto.socket.*;
import com.tikitaka.api.repository.*;
import com.tikitaka.api.domain.user.User;
import com.tikitaka.api.domain.lecture.Lecture;
import com.tikitaka.api.domain.react.*;
import com.tikitaka.api.domain.question.*;
import com.tikitaka.api.domain.comment.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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
                .build();
        commentRepository.save(comment);

        // ✅ 자동으로 질문 상태를 답변 완료로 변경
        question.updateStatus(QuestionStatus.ANSWERED);
    }

    private void handleAnswered(AnsweredSocketRequest request) {
        Question question = questionRepository.findById(request.getQuestionId())
                .orElseThrow(() -> new RuntimeException("Question not found with id: " + request.getQuestionId()));

        question.updateStatus(QuestionStatus.ANSWERED);
    }

    private void handleReaction(ReactSocketRequest request, String type, Long userId) {
        switch (type) {
            case "like" -> toggleReaction(request, userId, ReactType.LIKE);
            case "wonder" -> toggleReaction(request, userId, ReactType.WONDER);
            case "medal" -> toggleReaction(request, userId, ReactType.MEDAL);
        }
    }

    private void toggleReaction(ReactSocketRequest request, Long userId, ReactType reactType) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        Question question = questionRepository.findById(request.getQuestionId())
                .orElseThrow(() -> new RuntimeException("Question not found with id: " + request.getQuestionId()));

        Optional<React> existing = reactRepository.findByUserAndTargetAndType(user, question, reactType);

        if (existing.isPresent()) {
            reactRepository.delete(existing.get()); // 리액션 취소
        } else {
            React react = React.builder()
                    .user(user)
                    .target(question)
                    .type(reactType)
                    .build();
            reactRepository.save(react); // 리액션 등록
        }
    }
}
