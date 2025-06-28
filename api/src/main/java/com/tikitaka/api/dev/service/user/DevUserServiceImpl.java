package com.tikitaka.api.dev.service.user;

import com.tikitaka.api.dev.dto.UserRequest;
import com.tikitaka.api.dev.dto.UserResponse;
import com.tikitaka.api.domain.user.User;
import com.tikitaka.api.jwt.JwtTokenProvider;
import com.tikitaka.api.dev.repository.DevUserRepository;
import lombok.RequiredArgsConstructor;
import com.tikitaka.api.dev.dto.UserInfoResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DevUserServiceImpl implements DevUserService {
    private final DevUserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;


    @Override
    public UserResponse registerUser(UserRequest request) {
        User user = User.builder()
                .email(request.getEmail())
                .name(request.getName())
                .role(request.getRole())
                .studentId(request.getStudentId())
                .avatarUrl(request.getAvatarUrl())
                .sub(request.getSub())
                .build();
        userRepository.save(user);
        String token = jwtTokenProvider.createToken(user.getSub(), user.getRole());
        UserResponse response = new UserResponse();
        response.setToken(token);
        return response;
    }
    @Override
    public List<UserInfoResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> {
                    UserInfoResponse response = new UserInfoResponse();
                    response.setId(user.getId());
                    response.setEmail(user.getEmail());
                    response.setName(user.getName());
                    response.setRole(user.getRole().name());
                    response.setStudentId(user.getStudentId());
                    response.setAvatarUrl(user.getAvatarUrl());
                    response.setSub(user.getSub());
                    return response;
                })
                .collect(Collectors.toList());
    }
}
