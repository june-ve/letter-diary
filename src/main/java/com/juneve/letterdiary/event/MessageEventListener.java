package com.juneve.letterdiary.event;

import com.juneve.letterdiary.entity.DiaryMessage;
import com.juneve.letterdiary.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MessageEventListener {

    private final EmailService emailService;

    @Async
    @EventListener
    public void onMessageCreated(MessageCreatedEvent event) {

        DiaryMessage message = event.getMessage();
        String threadTitle = message.getThread().getTitle();

        // 상대방 이메일 찾기
        String senderEmail = message.getSender().getEmail();
        String userAEmail = message.getThread().getUserA().getEmail();
        String userBEmail = message.getThread().getUserB().getEmail();

        String targetEmail = senderEmail.equals(userAEmail) ? userBEmail : userAEmail;

        String previewContent = message.getContent().length() > 30
                ? message.getContent().substring(0, 30) + "..."
                : message.getContent();

        log.info("📨 [ASYNC] 이메일 알림 전송 시작");

        emailService.sendNewMessageAlert(targetEmail, threadTitle, previewContent);

        log.info("📨 [ASYNC] 이메일 알림 전송 완료");
    }
}
