package com.tikitaka.api.controller;

import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tikitaka.api.domain.user.User;
import com.tikitaka.api.domain.user.UserRole;
import com.tikitaka.api.dto.verify.OAuth2VerificationData;
import com.tikitaka.api.jwt.JwtTokenProvider;
import com.tikitaka.api.repository.UserRepository;
import com.tikitaka.api.service.email.EmailService;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final EmailService emailService;

    @PostMapping("/api/auth/code")
    public ResponseEntity<?> sendVerificationCode(@RequestParam String sub,
            @RequestParam String studentId) {
        String json = redisTemplate.opsForValue().get("verify:" + sub);
        if (json != null) {
            try {
                OAuth2VerificationData data = objectMapper.readValue(json, OAuth2VerificationData.class);
                String storedCode = data.getCode();
                emailService.sendEmail(
                        studentId + "@dankook.ac.kr",
                        "[티키타카] 인증코드입니다",
                        "아래 인증코드를 5분 이내에 입력해주세요:\n\n" + storedCode);
                return ResponseEntity.ok(Map.of(
                    "status", "SUCCESS",
                    "message", "인증 코드가 이메일로 전송되었습니다."
                ));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "ERROR",
                    "message", "인증 코드 전송 실패"
                ));
            }
        }
        return ResponseEntity.badRequest().body(Map.of(
            "status", "ERROR",
            "message", "유효하지 않은 요청입니다."
        ));
    }

    @PostMapping("/api/auth/verify")
    public ResponseEntity<?> verifyStudent(
            @RequestParam String sub,
            @RequestParam String code,
            @RequestParam String studentId) {
        // 인증 코드 검증
        String json = redisTemplate.opsForValue().get("verify:" + sub);
        if (json != null) {
            try {
                OAuth2VerificationData data = objectMapper.readValue(json, OAuth2VerificationData.class);
                String storedCode = data.getCode();
                if (storedCode == null || !storedCode.equals(code)) {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("인증 실패");
                }
                UserRole role = determineRoleFromStudentId(studentId);
                User user = userRepository.save(User.builder()
                        .email(data.getEmail())
                        .name(data.getName())
                        .studentId(studentId)
                        .sub(sub)
                        .role(role)
                        .build());
                String token = jwtTokenProvider.createToken(user.getEmail(), user.getRole());
                Map<String, Object> responseBody = Map.of(
                    "status", "SUCCESS",
                    "token", token,
                    "user", Map.of(
                        "id", user.getId(),
                        "email", user.getEmail(),
                        "name", user.getName(),
                        "studentId", user.getStudentId(),
                        "role", user.getRole().name()
                    )
                );
                return ResponseEntity.ok().body(responseBody);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("인증 처리 중 오류 발생");
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("인증 실패");
    }

    private UserRole determineRoleFromStudentId(String studentId) {
        if (studentId.startsWith("1")) {
            return UserRole.PROFESSOR;
        } else if (studentId.startsWith("3")) {
            return UserRole.STUDENT;
        } else {
            throw new IllegalArgumentException("올바르지 않은 학번입니다.");
        }
    }
}
