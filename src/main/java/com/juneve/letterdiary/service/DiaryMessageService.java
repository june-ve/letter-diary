package com.juneve.letterdiary.service;

import com.juneve.letterdiary.dto.request.DiaryMessageRequest;
import com.juneve.letterdiary.dto.response.DiaryMessagePageResponse;
import com.juneve.letterdiary.entity.DiaryMessage;
import com.juneve.letterdiary.entity.DiaryThread;
import com.juneve.letterdiary.entity.User;
import com.juneve.letterdiary.event.MessageCreatedEvent;
import com.juneve.letterdiary.repository.DiaryMessageRepository;
import com.juneve.letterdiary.repository.DiaryThreadRepository;
import com.juneve.letterdiary.validator.DiaryValidator;
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
    private final DiaryValidator diaryValidator;

    /**
     * 특정 일기장에 메시지 작성 (즉시 작성하고 비동기 이벤트 발생)
     */
    public DiaryMessage writeMessage(User sender, Long threadId, DiaryMessageRequest request) {
        DiaryThread thread = threadRepository.findThreadById(threadId);
        diaryValidator.validateParticipant(sender, thread);

        DiaryMessage message = DiaryMessage.of(request.getContent(), sender, thread);
        DiaryMessage savedMessage = messageRepository.save(message);

        eventPublisher.publishEvent(
                new MessageCreatedEvent(
                        thread.getTitle(),
                        identifyTargetEmail(sender, thread),
                        createPreviewContent(request.getContent())
                )
        );

        return savedMessage;
    }

    private String identifyTargetEmail(User sender, DiaryThread thread) {
        return sender.equals(thread.getUserA())
                ? thread.getUserB().getEmail()
                : thread.getUserA().getEmail();
    }

    private String createPreviewContent(String content) {
        return content.length() > 30
                ? content.substring(0, 30) + "..."
                : content;
    }

    /**
    * 특정 일기장의 메시지 한 개 조회
    */
    @Transactional(readOnly = true)
    public DiaryMessagePageResponse getMessagePage(User loginUser, long threadId, int page) {

        DiaryThread thread = threadRepository.findThreadById(threadId);
        diaryValidator.validateParticipant(loginUser, thread);

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
