package com.juneve.letterdiary.event;

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

        log.info("📨 [ASYNC] 이메일 알림 전송 시작");

        emailService.sendNewMessageAlert(
                event.targetEmail(), event.threadTitle(), event.previewContent()
        );

        log.info("📨 [ASYNC] 이메일 알림 전송 완료");
    }
}
