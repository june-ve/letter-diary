package com.juneve.letterdiary.controller;

import com.juneve.letterdiary.dto.response.DiaryThreadListResponse;
import com.juneve.letterdiary.dto.response.DiaryThreadResponse;
import com.juneve.letterdiary.entity.DiaryThread;
import com.juneve.letterdiary.entity.User;
import com.juneve.letterdiary.service.DiaryThreadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/threads")
public class DiaryThreadController {

    private final DiaryThreadService diaryThreadService;

    /**
     * 교환일기 생성
     * POST /api/threads/{userBId}
     */
    @PostMapping("/{userBId}")
    public ResponseEntity<DiaryThreadResponse> createThread(
            @AuthenticationPrincipal User userA,
            @PathVariable Long userBId) {

        DiaryThread thread = diaryThreadService.createThread(userA, userBId);
        DiaryThreadResponse response = DiaryThreadResponse.from(thread);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    /**
     * 사용자가 참여 중인 교환일기 목록 조회
     * GET /api/threads
     */
    @GetMapping
    public ResponseEntity<List<DiaryThreadListResponse>> getMyThreads(
            @AuthenticationPrincipal User user) {

        List<DiaryThreadListResponse> responses = diaryThreadService.getThreadsByUser(user);
        return ResponseEntity.ok(responses);
    }
}
