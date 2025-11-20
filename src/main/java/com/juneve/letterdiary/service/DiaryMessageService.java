package com.juneve.letterdiary.service;

import com.juneve.letterdiary.dto.request.DiaryMessageRequest;
import com.juneve.letterdiary.dto.response.DiaryMessagePageResponse;
import com.juneve.letterdiary.entity.DiaryMessage;
import com.juneve.letterdiary.entity.DiaryThread;
import com.juneve.letterdiary.entity.User;
import com.juneve.letterdiary.event.MessageCreatedEvent;
import com.juneve.letterdiary.repository.DiaryMessageRepository;
import com.juneve.letterdiary.repository.DiaryThreadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class DiaryMessageService {

    private final DiaryMessageRepository messageRepository;
    private final DiaryThreadRepository threadRepository;
    private final ApplicationEventPublisher eventPublisher;

    /**
     * 특정 일기장에 메시지 작성 (즉시 작성하고 비동기 이벤트 발생)
     */
    public DiaryMessage writeMessage(User sender, Long threadId, DiaryMessageRequest request) {
        DiaryThread thread = findThreadById(threadId);
        validateParticipant(sender, thread);

        DiaryMessage message = DiaryMessage.of(request.getContent(), sender, thread);
        DiaryMessage savedMessage = messageRepository.save(message);

        eventPublisher.publishEvent(new MessageCreatedEvent(savedMessage));

        return savedMessage;
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

    /**
    * 특정 일기장의 메시지 한 개 조회
    */
    @Transactional(readOnly = true)
    public DiaryMessagePageResponse getMessagePage(User loginUser, long threadId, int page) {

        DiaryThread thread = findThreadById(threadId);
        validateParticipant(loginUser, thread);

        Page<DiaryMessage> messagePage = findPagedMessage(thread, page);
        DiaryMessage message = messagePage.hasContent()
                ? messagePage.getContent().getFirst()
                : null;

        return DiaryMessagePageResponse.from(
                message,
                messagePage.getNumber(),
                messagePage.getTotalPages()
        );
    }

    private Page<DiaryMessage> findPagedMessage(DiaryThread thread, int page) {
        PageRequest pageable = PageRequest.of(page, 1);
        return messageRepository.findByThreadOrderByCreatedAtDesc(thread, pageable);
    }
}
