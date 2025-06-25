package com.tikitaka.api.dev.service;

import com.tikitaka.api.dev.dto.UserRequest;
import com.tikitaka.api.domain.user.User;
import com.tikitaka.api.dev.repository.DevUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DevUserServiceImpl implements DevUserService {
    private final DevUserRepository userRepository;

    @Override
    public void registerUser(UserRequest request) {
        User user = User.builder()
                .email(request.getEmail())
                .name(request.getName())
                .role(request.getRole())
                .studentId(request.getStudentId())
                .avatarUrl(request.getAvatarUrl())
                .sub(request.getSub())
                .build();
        userRepository.save(user);
    }
}
