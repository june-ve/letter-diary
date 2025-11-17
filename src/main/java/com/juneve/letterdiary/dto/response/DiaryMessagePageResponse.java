package com.juneve.letterdiary.dto.response;

import com.juneve.letterdiary.entity.DiaryMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class DiaryMessagePageResponse {

    private Long messageId;
    private String content;
    private String senderNickname;
    private LocalDateTime createdAt;

    private int currentPage;
    private int totalPages;

    public static DiaryMessagePageResponse from(DiaryMessage message, int currentPage, int totalPages) {

        // null 가능 (UI에서 '아직 작성된 메시지가 없습니다'로 표현)
        if (message == null) {
            return new DiaryMessagePageResponse(
                    null,
                    null,
                    null,
                    null,
                    currentPage,
                    totalPages
            );
        }

        return new DiaryMessagePageResponse(
                message.getId(),
                message.getContent(),
                message.getSender().getNickname(),
                message.getCreatedAt(),
                currentPage,
                totalPages
        );
    }
}
