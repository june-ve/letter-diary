package com.juneve.letterdiary.controller;

import com.juneve.letterdiary.dto.request.CreateThreadRequest;
import com.juneve.letterdiary.dto.response.DiaryDownloadResponse;
import com.juneve.letterdiary.dto.response.DiaryThreadListResponse;
import com.juneve.letterdiary.dto.response.DiaryThreadResponse;
import com.juneve.letterdiary.entity.DiaryThread;
import com.juneve.letterdiary.entity.User;
import com.juneve.letterdiary.service.DiaryThreadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/threads")
public class DiaryThreadController {

    private final DiaryThreadService diaryThreadService;

    /**
     * 교환일기 생성
     * POST /api/threads
     */
    @PostMapping
    public ResponseEntity<DiaryThreadResponse> createThread(
            @AuthenticationPrincipal User userA,
            @RequestBody CreateThreadRequest request) {

        DiaryThread thread = diaryThreadService.createThread(userA, request.getPartnerEmail());
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

    @GetMapping("/{threadId}/download")
    public ResponseEntity<byte[]> downloadThread(
            @AuthenticationPrincipal User user,
            @PathVariable Long threadId) {

        DiaryDownloadResponse response = diaryThreadService.createDownloadData(user, threadId);

        byte[] bytes = response.content().getBytes(StandardCharsets.UTF_8);

        // 파일명 인코딩 처리
        ContentDisposition contentDisposition = ContentDisposition.builder("attachment")
                .filename(response.filename(), StandardCharsets.UTF_8)
                .build();

        return ResponseEntity.ok()
                .headers(headers -> headers.setContentDisposition(contentDisposition))
                .contentType(MediaType.TEXT_PLAIN)
                .body(bytes);
    }
}
