package com.juneve.letterdiary.controller;

import com.juneve.letterdiary.dto.response.DiaryThreadResponse;
import com.juneve.letterdiary.entity.DiaryThread;
import com.juneve.letterdiary.entity.User;
import com.juneve.letterdiary.service.DiaryThreadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/thread")
public class DiaryThreadController {

    private final DiaryThreadService diaryThreadService;

    @PostMapping("/create/{userBId}")
    public ResponseEntity<DiaryThreadResponse> createThread(
            @AuthenticationPrincipal User userA,
            @PathVariable Long userBId) {

        DiaryThread thread = diaryThreadService.createThread(userA, userBId);
        DiaryThreadResponse response = DiaryThreadResponse.from(thread);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
}
