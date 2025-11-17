package com.juneve.letterdiary.dto.response;

import com.juneve.letterdiary.entity.DiaryMessage;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class DiaryMessageResponse {

    private Long id;
    private String content;
    private String senderNickname;
    private Long threadId;
    private LocalDateTime createdAt;

    public static DiaryMessageResponse from(DiaryMessage message) {
        return DiaryMessageResponse.builder()
                .id(message.getId())
                .content(message.getContent())
                .senderNickname(message.getSender().getNickname())
                .threadId(message.getThread().getId())
                .createdAt(message.getCreatedAt())
                .build();
    }
}
