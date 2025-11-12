package com.juneve.letterdiary.service;

import com.juneve.letterdiary.dto.request.UserLoginRequest;
import com.juneve.letterdiary.dto.request.UserSignupRequest;
import com.juneve.letterdiary.entity.User;
import com.juneve.letterdiary.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void signup(UserSignupRequest request) {
        validateEmailUniqueness(request.getEmail());
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        userRepository.save(request.toEntity(encodedPassword));
    }

    @Transactional(readOnly = true)
    public User login(UserLoginRequest request) {
        User user = findUserByEmail(request.getEmail());
        validatePassword(request.getPassword(), user.getPassword());
        return user;
    }

    private void validateEmailUniqueness(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("이미 가입된 이메일입니다.");
        }
    }

    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 이메일입니다."));
    }

    private void validatePassword(String rawPassword, String encodedPassword) {
        if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
    }
}
