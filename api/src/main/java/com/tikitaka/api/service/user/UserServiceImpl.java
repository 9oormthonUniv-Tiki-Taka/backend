package com.tikitaka.api.service.user;

import com.tikitaka.api.domain.point.PointTransaction;
import com.tikitaka.api.domain.point.PointType;
import com.tikitaka.api.domain.question.Question;
import com.tikitaka.api.domain.react.React;
import com.tikitaka.api.domain.react.ReactType;
import com.tikitaka.api.domain.report.Report;
import com.tikitaka.api.domain.user.User;
import com.tikitaka.api.dto.user.*;
import com.tikitaka.api.repository.PointTransactionRepository;
import com.tikitaka.api.repository.QuestionRepository;
import com.tikitaka.api.repository.ReactRepository;
import com.tikitaka.api.repository.ReportRepository;
import com.tikitaka.api.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PointTransactionRepository pointTransactionRepository;
    private final QuestionRepository questionRepository;
    private final ReactRepository reactRepository;
    private final ReportRepository reportRepository;

    @Override
    public UserInfoResponse getMyInfo(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        Long spend = pointTransactionRepository.countByUserIdAndType(userId, PointType.SPEND);
        Long earn = pointTransactionRepository.countByUserIdAndType(userId, PointType.EARN);

        UserInfoResponse.UserDto userDto = new UserInfoResponse.UserDto();
        userDto.setAvatar(user.getAvatarUrl());
        userDto.setEmail(user.getEmail());
        userDto.setName(user.getName());
        userDto.setStudentId(user.getStudentId());
        userDto.setPoint(earn - spend);

        UserInfoResponse response = new UserInfoResponse();
        response.setUser(userDto);

        return response;
    }

    @Override
    public UserPointResponse getMyPoints(Long userId, String type, Pageable pageable) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        switch (type) {
            case "earn" -> {
                Page<PointTransaction> transactions = pointTransactionRepository
                        .findByUserIdAndTypeOrderByCreatedAtDesc(userId, PointType.EARN, pageable);
                return toUserPointResponse(user, transactions);
            }
            case "spend" -> {
                Page<PointTransaction> transactions = pointTransactionRepository
                        .findByUserIdAndTypeOrderByCreatedAtDesc(userId, PointType.SPEND, pageable);
                return toUserPointResponse(user, transactions);
            }
            case "all" -> {
                Page<PointTransaction> transactions = pointTransactionRepository
                        .findByUserIdOrderByCreatedAtDesc(userId, pageable);
                return toUserPointResponse(user, transactions);
            }
        }
        return null;
    }

    public UserPointResponse toUserPointResponse(User user, Page<PointTransaction> transactions) {
        UserPointResponse response = new UserPointResponse();
        Long earn = pointTransactionRepository.countByUserIdAndType(user.getId(), PointType.EARN);
        Long spend = pointTransactionRepository.countByUserIdAndType(user.getId(), PointType.SPEND);
        response.setPoint(earn - spend);

        Page<UserPointResponse.PointDto> pointDtos = transactions.map(pt -> {
            UserPointResponse.PointDto dto = new UserPointResponse.PointDto();
            dto.setId(String.valueOf(pt.getPointId()));
            dto.setAmount(pt.getAmount());
            dto.setType(pt.getType().name().toLowerCase());
            dto.setReason(pt.getReason());
            dto.setCreatedAt(pt.getCreatedAt());
            return dto;
        });

        response.setPoints(pointDtos);
        return response;
    }

    @Override
    public UserQuestionResponse getMyQuestions(Long userId, Pageable pageable) {
        Page<Question> questions = questionRepository.findByUserId(userId, pageable);

        Page<UserQuestionResponse.QuestionDto> dtos = questions.map(q -> {
                    UserQuestionResponse.QuestionDto dto = new UserQuestionResponse.QuestionDto();
                    dto.setId(q.getQuestionId().toString());
                    dto.setLectureName(q.getLecture().getName());
                    dto.setContent(q.getContent());
                    dto.setStatus(q.getStatus().toString());
                    dto.setCreatedAt(q.getCreatedAt());
                    return dto;
                });

        UserQuestionResponse response = new UserQuestionResponse();
        response.setQuestions(dtos);

        return response;
    }

    @Override
    public UserReactResponse getMyReacts(Long userId, String type, Pageable pageable) {
        Page<React> reacts;

        if (type == null || type.equals("all")) {
            reacts = reactRepository.findByUserId(userId, pageable);
        } else {
            ReactType reactType = ReactType.valueOf(type.toUpperCase());
            reacts = reactRepository.findByUserIdAndType(userId, reactType, pageable);
        }

        Page<UserReactResponse.ReactDto> dtos = reacts.map(r -> {
                    UserReactResponse.ReactDto dto = new UserReactResponse.ReactDto();
                    dto.setId(r.getReactId().toString());
                    dto.setType(r.getType().name().toLowerCase());
                    dto.setLectureName(r.getTarget().getLecture().getName());
                    dto.setCreatedAt(r.getCreatedAt());
                    return dto;
                });
        UserReactResponse response = new UserReactResponse();
        response.setReacts(dtos);
        return response;
    }

    @Override
    public UserReportResponse getMyReports(Long userId, Pageable pageable) {
        Page<Report> reports = reportRepository.findByReporterId(userId, pageable);

        Page<UserReportResponse.ReportDto> dtos = reports.map(r -> {
                    UserReportResponse.ReportDto dto = new UserReportResponse.ReportDto();
                    dto.setId(r.getReportId().toString());
                    dto.setType(r.getTargetType().toString());
                    dto.setStatus(r.getStatus().name().toLowerCase());
                    dto.setReason(r.getReason());
                    dto.setCreatedAt(r.getCreatedAt());
                    return dto;
                });

        UserReportResponse response = new UserReportResponse();
        response.setReport(dtos);
        return response;
    }

}
