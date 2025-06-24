package com.tikitaka.api.util;

import java.time.Duration;
import java.util.Random;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.tikitaka.api.dto.verify.OAuth2VerificationData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VerificationService {

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    public void saveVerificationData(String email, String sub, String name) {
        String code = generateCode();
        OAuth2VerificationData data = OAuth2VerificationData.builder()
                .code(code)
                .email(email)
                .sub(sub)
                .name(name)
                .build();
        try {
            String json = objectMapper.writeValueAsString(data);
            redisTemplate.opsForValue().set(buildKey(sub), json, Duration.ofMinutes(5)); // 5분 TTL
        } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize verification data", e);
        }
    }

    public boolean verifyCode(String sub, String inputCode) {
        String key = buildKey(sub);
        String json = redisTemplate.opsForValue().get(key);
        if (json != null) {
            try {
                OAuth2VerificationData data = objectMapper.readValue(json, OAuth2VerificationData.class);
                String storedCode = data.getCode();
                if (storedCode != null && storedCode.equals(inputCode)) {
                    redisTemplate.delete(key);
                    return true;
                }
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private String buildKey(String sub) {
        return "verify:" + sub;
    }

    private String generateCode() {
        return String.valueOf(new Random().nextInt(900_000) + 100_000); // 6자리 숫자
    }
}
