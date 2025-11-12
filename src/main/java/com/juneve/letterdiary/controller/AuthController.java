package com.juneve.letterdiary.controller;

import com.juneve.letterdiary.dto.request.UserLoginRequest;
import com.juneve.letterdiary.dto.request.UserSignupRequest;
import com.juneve.letterdiary.entity.User;
import com.juneve.letterdiary.security.JwtTokenProvider;
import com.juneve.letterdiary.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@Valid @RequestBody UserSignupRequest request) {
        authService.signup(request);
        return ResponseEntity.ok("회원가입이 완료되었습니다.");
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@Valid @RequestBody UserLoginRequest request) {
        User user = authService.login(request);
        String token = jwtTokenProvider.generateToken(user.getEmail());

        return ResponseEntity.ok(Map.of(
                "message", "로그인 성공",
                "token", token
        ));
    }
}
