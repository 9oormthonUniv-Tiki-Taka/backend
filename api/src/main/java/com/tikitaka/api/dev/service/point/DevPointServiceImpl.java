package com.tikitaka.api.dev.service;

import com.tikitaka.api.dev.dto.PointRequest;
import com.tikitaka.api.domain.point.PointTransaction;
import com.tikitaka.api.domain.user.User;
import com.tikitaka.api.dev.repository.DevPointRepository;
import com.tikitaka.api.dev.repository.DevUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DevPointServiceImpl implements DevPointService {
    private final DevPointRepository pointRepository;
    private final DevUserRepository userRepository;

    @Override
    public void registerPoint(PointRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        PointTransaction point = PointTransaction.builder()
                .user(user)
                .amount(request.getAmount())
                .reason(request.getReason())
                .type(request.getType())
                .build();

        pointRepository.save(point);
    }
}
