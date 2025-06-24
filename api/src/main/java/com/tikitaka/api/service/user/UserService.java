package com.tikitaka.api.service.user;

import org.springframework.data.domain.Pageable;

import com.tikitaka.api.dto.user.UserInfoResponse;
import com.tikitaka.api.dto.user.UserPointResponse;
import com.tikitaka.api.dto.user.UserQuestionResponse;
import com.tikitaka.api.dto.user.UserReactResponse;
import com.tikitaka.api.dto.user.UserReportResponse;


public interface UserService {
    UserInfoResponse getMyInfo(Long userId);
    UserQuestionResponse getMyQuestions(Long userId, Pageable pageable);
    UserReactResponse getMyReacts(Long userId, String type, Pageable pageable);
    UserReportResponse getMyReports(Long userId, Pageable pageable);
    UserPointResponse getMyPoints(Long userId, String type, Pageable pageable);
}
