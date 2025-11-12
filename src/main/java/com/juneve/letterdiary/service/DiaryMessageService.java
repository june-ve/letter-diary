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

    private final DiaryMessageRepository diaryMessageRepository;
    private final DiaryThreadRepository diaryThreadRepository;

    public DiaryMessage writeMessage(User sender, DiaryMessageRequest request) {
        DiaryThread thread = findThreadById(request.getThreadId());
        validateParticipant(sender, thread);

        DiaryMessage message = DiaryMessage.of(request.getContent(), sender, thread);
        return diaryMessageRepository.save(message);
    }

    private DiaryThread findThreadById(Long threadId) {
        return diaryThreadRepository.findById(threadId)
                .orElseThrow(() -> new IllegalArgumentException("해당 일기장이 존재하지 않습니다."));
    }

    private void validateParticipant(User sender, DiaryThread thread) {
        boolean isParticipant = thread.getUserA().equals(sender) || thread.getUserB().equals(sender);
        if (!isParticipant) {
            throw new IllegalStateException("이 일기장에 작성할 권한이 없습니다.");
        }
    }
}
