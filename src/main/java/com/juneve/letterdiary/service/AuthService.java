package com.juneve.letterdiary.service;

import com.juneve.letterdiary.dto.request.UserLoginRequest;
import com.juneve.letterdiary.dto.request.UserSignupRequest;
import com.juneve.letterdiary.entity.User;
import com.juneve.letterdiary.repository.UserRepository;
import com.juneve.letterdiary.validator.UserValidator;
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
    private final UserValidator userValidator;

    public void signup(UserSignupRequest request) {
        userValidator.validateEmailUniqueness(request.getEmail());
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        userRepository.save(request.toEntity(encodedPassword));
    }

    @Transactional(readOnly = true)
    public User login(UserLoginRequest request) {
        User user = userRepository.findUserByEmail(request.getEmail());
        userValidator.validatePassword(request.getPassword(), user.getPassword());
        return user;
    }
}
