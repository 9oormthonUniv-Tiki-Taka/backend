package com.tikitaka.api.service.socket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tikitaka.api.domain.comment.Comment;
import com.tikitaka.api.domain.lecture.Lecture;
import com.tikitaka.api.domain.question.Question;
import com.tikitaka.api.domain.question.QuestionStatus;
import com.tikitaka.api.domain.react.React;
import com.tikitaka.api.domain.react.ReactType;
import com.tikitaka.api.domain.report.*;
import com.tikitaka.api.domain.user.User;
import com.tikitaka.api.dto.socket.*;
import com.tikitaka.api.repository.*;
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
    private final ReportRepository reportRepository;

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
            case "report" -> {
                ReportSocketRequest req = convert(message, ReportSocketRequest.class);
                handleReport(req, userId);
            }
            default -> log.warn("Unknown socket message type: {}", message.getType());
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

        question.updateStatus(QuestionStatus.ANSWERED);
    }

    private void handleAnswered(AnsweredSocketRequest request) {
        Question question = questionRepository.findById(request.getQuestionId())
                .orElseThrow(() -> new RuntimeException("Question not found with id: " + request.getQuestionId()));

        question.updateStatus(QuestionStatus.ANSWERED);
    }

    private void handleReaction(ReactSocketRequest request, String type, Long userId) {
        ReactType reactType;
        switch (type) {
            case "like" -> reactType = ReactType.LIKE;
            case "wonder" -> reactType = ReactType.WONDER;
            case "medal" -> reactType = ReactType.MEDAL;
            default -> throw new IllegalArgumentException("Invalid reaction type: " + type);
        }
        toggleReaction(request, userId, reactType);
    }

    private void toggleReaction(ReactSocketRequest request, Long userId, ReactType reactType) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        Question question = questionRepository.findById(request.getQuestionId())
                .orElseThrow(() -> new RuntimeException("Question not found with id: " + request.getQuestionId()));

        Optional<React> existing = reactRepository.findByUserAndTargetAndType(user, question, reactType);

        if (existing.isPresent()) {
            reactRepository.delete(existing.get());
        } else {
            React react = React.builder()
                    .user(user)
                    .target(question)
                    .type(reactType)
                    .build();
            reactRepository.save(react);
        }
    }

    @Transactional
    public void handleReport(ReportSocketRequest request, Long userId) {
        User reporter = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        Question question = questionRepository.findById(request.getTargetId())
                .orElseThrow(() -> new RuntimeException("Question not found with id: " + request.getTargetId()));

        boolean alreadyReported = reportRepository.existsByReporterAndTargetTypeAndTargetId(
                reporter, ReportType.QUESTION, request.getTargetId());

        if (alreadyReported) {
            throw new RuntimeException("You have already reported this question.");
        }

        Report report = Report.builder()
                .reporter(reporter)
                .reportedUser(question.getUser())
                .targetType(ReportType.QUESTION)
                .targetId(request.getTargetId())
                .reason(request.getReason())
                .status(ReportStatus.신고완료)
                .build();

        reportRepository.save(report);

        log.info("User {} reported question {} successfully.", userId, request.getTargetId());
    }
}
