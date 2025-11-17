package com.juneve.letterdiary.service;

import com.juneve.letterdiary.dto.response.DiaryThreadListResponse;
import com.juneve.letterdiary.entity.DiaryMessage;
import com.juneve.letterdiary.entity.DiaryThread;
import com.juneve.letterdiary.entity.User;
import com.juneve.letterdiary.repository.DiaryThreadRepository;
import com.juneve.letterdiary.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class DiaryThreadService {

    private final DiaryThreadRepository diaryThreadRepository;
    private final UserRepository userRepository;

    /**
     * 교환일기 생성
     */
    public DiaryThread createThread(User userA, Long userBId) {
        User userB = findUserById(userBId);
        validateDuplicateThread(userA, userB);

        String title = generateTitle(userA, userB);
        DiaryThread thread = buildDiaryThread(userA, userB, title);

        return diaryThreadRepository.save(thread);
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("상대 사용자가 존재하지 않습니다."));
    }

    /**
     * 두 사용자 간 중복된 일기장 존재 여부 확인
     */
    private void validateDuplicateThread(User userA, User userB) {
        boolean exists = diaryThreadRepository.findAll().stream()
                .anyMatch(t ->
                        (t.getUserA().equals(userA) && t.getUserB().equals(userB)) ||
                        (t.getUserA().equals(userB) && t.getUserB().equals(userA))
                );
        if (exists) {
            throw new IllegalArgumentException("이미 교환일기가 존재합니다.");
        }
    }

    private String generateTitle(User userA, User userB) {
        return userA.getNickname() + " & " + userB.getNickname() +"의 교환일기";
    }

    private DiaryThread buildDiaryThread(User userA, User userB, String title) {
        return DiaryThread.builder()
                .userA(userA)
                .userB(userB)
                .title(title)
                .build();
    }

    /**
     * 사용자가 참여 중인 교환일기 목록 조회
     */
    @Transactional(readOnly = true)
    public List<DiaryThreadListResponse> getThreadsByUser(User user) {
        List<DiaryThread> threads = diaryThreadRepository.findAllByUser(user);

        return threads.stream()
                .sorted(Comparator.comparing(this::extractLastMessageTime))
                .map(DiaryThreadListResponse::from)
                .toList();
    }

    private LocalDateTime extractLastMessageTime(DiaryThread thread) {
        return thread.getMessages().stream()
                .map(DiaryMessage::getCreatedAt)
                .max(LocalDateTime::compareTo)
                .orElse(LocalDateTime.MIN);
    }
}
