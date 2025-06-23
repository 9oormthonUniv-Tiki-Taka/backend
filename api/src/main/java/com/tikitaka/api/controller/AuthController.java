package com.tikitaka.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthController {

    @PostMapping("/api/signin")
    public ResponseEntity<?> signIn() {
        return ResponseEntity.ok("로그인 성공");
    }

    @PostMapping("/api/signout")
    public ResponseEntity<?> signOut() {
        return ResponseEntity.ok("로그아웃 성공");
    }
}
