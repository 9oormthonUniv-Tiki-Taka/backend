package com.tikitaka.api.service;

import com.tikitaka.api.domain.user.User;
import java.util.List;

public interface UserService {
    User getMyInfo(Long userId);
    List<?> getMyQuestions(Long userId);
    List<?> getMyReacts(Long userId);
    List<?> getMyReports(Long userId);
    List<?> getMyPoints(Long userId);
}
