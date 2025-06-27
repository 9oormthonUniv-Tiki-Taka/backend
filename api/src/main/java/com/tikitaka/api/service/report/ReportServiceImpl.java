package com.tikitaka.api.service.report;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tikitaka.api.domain.comment.Comment;
import com.tikitaka.api.domain.question.Question;
import com.tikitaka.api.domain.report.Report;
import com.tikitaka.api.domain.report.ReportStatus;
import com.tikitaka.api.domain.user.User;
import com.tikitaka.api.dto.report.ReportRequest;
import com.tikitaka.api.repository.CommentRepository;
import com.tikitaka.api.repository.QuestionRepository;
import com.tikitaka.api.repository.ReportRepository;
import com.tikitaka.api.repository.UserRepository;
import com.tikitaka.api.domain.report.ReportType;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;
    private final UserRepository userRepository;
    private final QuestionRepository questionRepository;
    private final CommentRepository commentRepository;

    @Override
    @Transactional
    public void reportContent(Long userId, ReportRequest request) {

        switch (request.getTargetType()) {
            case "question" -> handleQuestionReport(userId, request);
            case "comment" -> handleCommentReport(userId, request);
            default -> throw new IllegalArgumentException("Invalid report target type: " + request.getTargetType());
        }
    }

    private void handleQuestionReport(Long userId, ReportRequest request) {
        User reporter = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        Long targetId = request.getTargetId();
        ReportType type = ReportType.QUESTION;

        if (reportRepository.existsByReporterAndTargetTypeAndTargetId(reporter, type, targetId)) {
            throw new RuntimeException("이미 해당 질문을 신고하셨습니다.");
        }

        Question question = questionRepository.findById(targetId)
                .orElseThrow(() -> new RuntimeException("Question not found with id: " + targetId));

        Report report = Report.builder()
                .reporter(reporter)
                .reportedUser(question.getUser())
                .targetType(type)
                .targetId(targetId)
                .reason(request.getReason())
                .status(ReportStatus.신고완료)
                .build();

        reportRepository.save(report);
    }

    private void handleCommentReport(Long userId, ReportRequest request) {
        User reporter = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        Long targetId = request.getTargetId();
        ReportType type = ReportType.COMMENT;

        if (reportRepository.existsByReporterAndTargetTypeAndTargetId(reporter, type, targetId)) {
            throw new RuntimeException("이미 해당 댓글을 신고하셨습니다.");
        }

        Comment comment = commentRepository.findById(targetId)
                .orElseThrow(() -> new RuntimeException("Comment not found with id: " + targetId));

        Report report = Report.builder()
                .reporter(reporter)
                .reportedUser(comment.getResponder())
                .targetType(type)
                .targetId(targetId)
                .reason(request.getReason())
                .status(ReportStatus.신고완료)
                .build();

        reportRepository.save(report);
    }
}
