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

    @PostMapping("/api/code")
    public ResponseEntity<?> sendVerificationCode(@RequestParam String sub,
            @RequestParam String studentId) {
        String json = redisTemplate.opsForValue().get("verify:" + sub);
        if (json == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("임시 사용자 정보가 없습니다.");
        }
        if (json != null) {
            try {
                OAuth2VerificationData data = objectMapper.readValue(json, OAuth2VerificationData.class);
                String storedCode = data.getCode();
                emailService.sendEmail(
                        studentId + "@dankook.ac.kr",
                        "[티키타카] 인증코드입니다",
                        "아래 인증코드를 5분 이내에 입력해주세요:\n\n" + storedCode);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return ResponseEntity.ok("인증 코드가 이메일로 전송되었습니다.");
    }

    @PostMapping("/api/verify")
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
                        .role(role)
                        .build());
                String token = jwtTokenProvider.createToken(user.getEmail(), user.getRole());
                return ResponseEntity.ok().body(Map.of("token", token));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("인증 실패");
    }

    @PostMapping("/api/signin")
    public ResponseEntity<?> signIn() {
        return ResponseEntity.ok("로그인 성공");
    }

    @PostMapping("/api/signout")
    public ResponseEntity<?> signOut() {
        return ResponseEntity.ok("로그아웃 성공");
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
