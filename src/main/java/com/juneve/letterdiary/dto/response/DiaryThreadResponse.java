package com.juneve.letterdiary.dto.response;

import com.juneve.letterdiary.entity.DiaryThread;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DiaryThreadResponse {
    private Long id;
    private String title;
    private String userANickname;
    private String userBNickname;

    public static DiaryThreadResponse from(DiaryThread thread) {
        return new DiaryThreadResponse(
                thread.getId(),
                thread.getTitle(),
                thread.getUserA().getNickname(),
                thread.getUserB().getNickname()
        );
    }
}
