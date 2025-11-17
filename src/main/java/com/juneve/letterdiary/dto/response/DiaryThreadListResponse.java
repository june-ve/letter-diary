package com.juneve.letterdiary.dto.response;

import com.juneve.letterdiary.entity.DiaryMessage;
import com.juneve.letterdiary.entity.DiaryThread;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Comparator;

@Getter
@AllArgsConstructor
public class DiaryThreadListResponse {

    private Long id;
    private String title;
    private String lastMessageContent;
    private LocalDateTime lastMessageTime;

    /**
     * DiaryThread → DiaryThreadListResponse 변환
     * (최근 메시지가 있으면 해당 메시지를 기준으로, 없으면 기본 문구 설정)
     */
    public static DiaryThreadListResponse from(DiaryThread thread) {
        if (!hasMessages(thread)) {
            return buildWithoutLatestMessage(thread);
        }

        DiaryMessage latestMessage = findLatestMessage(thread);
        return buildWithLatestMessage(thread, latestMessage);
    }

    private static boolean hasMessages(DiaryThread thread) {
        return thread.getMessages() != null && !thread.getMessages().isEmpty();
    }

    private static DiaryMessage findLatestMessage(DiaryThread thread) {
        return thread.getMessages().stream()
                .max(Comparator.comparing(DiaryMessage::getCreatedAt))
                .orElse(null);
    }

    private static DiaryThreadListResponse buildWithoutLatestMessage(DiaryThread thread) {
        return new DiaryThreadListResponse(
                thread.getId(),
                thread.getTitle(),
                "(아직 작성된 일기가 없습니다.)",
                null
        );
    }

    private static DiaryThreadListResponse buildWithLatestMessage(
            DiaryThread thread,
            DiaryMessage latestMessage
    ) {
        return new DiaryThreadListResponse(
                thread.getId(),
                thread.getTitle(),
                latestMessage.getContent(),
                latestMessage.getCreatedAt()
        );
    }
}
