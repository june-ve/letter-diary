package com.juneve.letterdiary.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendNewMessageAlert(String to, String threadTitle, String previewContent) {

        String subject = "[Letter Diary] 💌 새 교환일기 알림";
        String text = """
                새로운 교환일기가 도착했습니다!

                일기장: %s

                내용 미리보기:
                %s

                서비스에서 확인해주세요 😊
                """.formatted(threadTitle, previewContent);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);

        mailSender.send(message);
        log.info("이메일 전송 성공! → {}", to);
    }
}
