package com.juneve.letterdiary.validator;

import com.juneve.letterdiary.entity.DiaryThread;
import com.juneve.letterdiary.entity.User;
import com.juneve.letterdiary.repository.DiaryThreadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DiaryValidator {

    private final DiaryThreadRepository threadRepository;

    public void validateParticipant(User user, DiaryThread thread) {
        boolean isParticipant = thread.getUserA().equals(user) || thread.getUserB().equals(user);
        if (!isParticipant) {
            throw new IllegalStateException("이 일기장에 접근할 권한이 없습니다.");
        }
    }

    public void validateDuplicateThread(User userA, User userB) {
        // (A, B) 순서이거나 (B, A) 순서인 경우를 모두 체크
        if (threadRepository.existsByUserAAndUserBOrUserAAndUserB(userA, userB, userB, userA)) {
            throw new IllegalArgumentException("이미 교환일기가 존재합니다.");
        }
    }
}
