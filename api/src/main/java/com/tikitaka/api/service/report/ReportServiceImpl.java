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
            case "question" -> {
                handleQuestionReport(userId, request);
            }
            case "comment" -> {
                handleCommentReport(userId, request);
            }
        }
    }

    private void handleQuestionReport(Long userId, ReportRequest request) {
        User reporter = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));        

        Question question = questionRepository.findById(request.getTargetId())
                .orElseThrow(() -> new RuntimeException("Question not found with id: " + request.getTargetId()));
        

        Report report = Report.builder()
                .reporter(reporter)
                .reportedUser(question.getUser())
                .targetType(ReportType.QUESTION)
                .targetId(request.getTargetId())
                .reason(request.getReason())
                .status(ReportStatus.신고완료)
                .build();

        Report saved = reportRepository.save(report);
    }

    private void handleCommentReport(Long userId, ReportRequest request) {
        User reporter = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        
        Comment comment = commentRepository.findById(request.getTargetId())
                .orElseThrow(() -> new RuntimeException("Comment not found with id: " + request.getTargetId()));
        
        Report report = Report.builder()
                .reporter(reporter)
                .reportedUser(comment.getResponder())
                .targetType(ReportType.COMMENT)
                .targetId(request.getTargetId())
                .reason(request.getReason())
                .status(ReportStatus.신고완료)
                .build();
        reportRepository.save(report);
    }   
}
