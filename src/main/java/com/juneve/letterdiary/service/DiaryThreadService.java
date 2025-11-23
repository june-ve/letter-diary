package com.juneve.letterdiary.service;

import com.juneve.letterdiary.dto.response.DiaryDownloadResponse;
import com.juneve.letterdiary.dto.response.DiaryThreadListResponse;
import com.juneve.letterdiary.entity.DiaryMessage;
import com.juneve.letterdiary.entity.DiaryThread;
import com.juneve.letterdiary.entity.User;
import com.juneve.letterdiary.repository.DiaryThreadRepository;
import com.juneve.letterdiary.repository.UserRepository;
import com.juneve.letterdiary.validator.DiaryValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class DiaryThreadService {

    private final DiaryThreadRepository threadRepository;
    private final UserRepository userRepository;
    private final DiaryValidator diaryValidator;

    /**
     * 교환일기 생성
     */
    public DiaryThread createThread(User userA, String partnerEmail) {
        User userB = userRepository.findUserByEmail(partnerEmail);
        diaryValidator.validateDuplicateThread(userA, userB);

        String title = generateTitle(userA, userB);
        DiaryThread thread = buildDiaryThread(userA, userB, title);

        return threadRepository.save(thread);
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
        List<DiaryThread> threads = threadRepository.findAllByUser(user);

        return threads.stream()
                .sorted(Comparator.comparing(this::extractLastMessageTime).reversed())
                .map(DiaryThreadListResponse::from)
                .toList();
    }

    private LocalDateTime extractLastMessageTime(DiaryThread thread) {
        return thread.getMessages().stream()
                .map(DiaryMessage::getCreatedAt)
                .max(LocalDateTime::compareTo)
                .orElse(LocalDateTime.MIN);
    }

    /**
     * 다운로드용 파일 데이터 생성 (파일명 + 본문)
     */
    @Transactional(readOnly = true)
    public DiaryDownloadResponse createDownloadData(User user, Long threadId) {
        DiaryThread thread = threadRepository.findThreadById(threadId);
        diaryValidator.validateParticipant(user, thread);

        String filename = generateSafeFilename(thread.getTitle());
        String content = buildContent(thread);

        return new DiaryDownloadResponse(filename, content);
    }

    private String generateSafeFilename(String title) {
        String safeTitle = title.replaceAll("[\\\\/:*?\"<>|\\s]", "");
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        return safeTitle + "_" + date + ".txt";
    }

    private String buildContent(DiaryThread thread) {
        List<DiaryMessage> messages = thread.getMessages().stream()
                .sorted(Comparator.comparing(DiaryMessage::getCreatedAt))
                .toList();

        StringBuilder sb = new StringBuilder();
        sb.append("📖 ").append(thread.getTitle()).append("\n");
        sb.append("=====================\n\n");

        if (messages.isEmpty()) {
            sb.append("(아직 작성된 메시지가 없습니다.)\n");
            return sb.toString();
        }

        for (DiaryMessage msg : messages) {
            String formattedDate = msg.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            sb.append("📆 ").append(formattedDate).append("\n");
            sb.append("👤 ").append(msg.getSender().getNickname()).append("\n");
            sb.append("--------------------------------------\n");
            sb.append(msg.getContent()).append("\n\n");
        }
        return sb.toString();
    }
}
