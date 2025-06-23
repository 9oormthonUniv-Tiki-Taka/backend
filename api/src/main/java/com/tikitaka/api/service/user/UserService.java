package com.tikitaka.api.service.user;

import com.tikitaka.api.dto.user.UserInfoResponse;
import com.tikitaka.api.dto.user.UserPointResponse;
import com.tikitaka.api.dto.user.UserQuestionResponse;
import com.tikitaka.api.dto.user.UserReactResponse;
import com.tikitaka.api.dto.user.UserReportResponse;


public interface UserService {
    UserInfoResponse getMyInfo(Long userId);
    UserQuestionResponse getMyQuestions(Long userId);
    UserReactResponse getMyReacts(Long userId, String type);
    UserReportResponse getMyReports(Long userId);
    UserPointResponse getMyPoints(Long userId, String type);
}
