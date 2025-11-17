package com.juneve.letterdiary.service;

import com.juneve.letterdiary.dto.request.DiaryMessageRequest;
import com.juneve.letterdiary.entity.DiaryMessage;
import com.juneve.letterdiary.entity.DiaryThread;
import com.juneve.letterdiary.entity.User;
import com.juneve.letterdiary.repository.DiaryMessageRepository;
import com.juneve.letterdiary.repository.DiaryThreadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class DiaryMessageService {

    private final DiaryMessageRepository messageRepository;
    private final DiaryThreadRepository threadRepository;

    /**
     * 특정 일기장에 메시지 작성
     */
    public DiaryMessage writeMessage(User sender, Long threadId, DiaryMessageRequest request) {
        DiaryThread thread = findThreadById(threadId);
        validateParticipant(sender, thread);

        DiaryMessage message = DiaryMessage.of(request.getContent(), sender, thread);
        return messageRepository.save(message);
    }

    private DiaryThread findThreadById(Long threadId) {
        return threadRepository.findById(threadId)
                .orElseThrow(() -> new IllegalArgumentException("해당 일기장이 존재하지 않습니다."));
    }

    private void validateParticipant(User user, DiaryThread thread) {
        boolean isParticipant = thread.getUserA().equals(user) || thread.getUserB().equals(user);
        if (!isParticipant) {
            throw new IllegalStateException("이 일기장에 접근할 권한이 없습니다.");
        }
    }
}
