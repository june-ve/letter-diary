package com.juneve.letterdiary.controller;

import com.juneve.letterdiary.dto.request.DiaryMessageRequest;
import com.juneve.letterdiary.dto.response.DiaryMessagePageResponse;
import com.juneve.letterdiary.dto.response.DiaryMessageResponse;
import com.juneve.letterdiary.entity.DiaryMessage;
import com.juneve.letterdiary.entity.User;
import com.juneve.letterdiary.service.DiaryMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/threads/{threadId}/messages")
public class DiaryMessageController {

    private final DiaryMessageService diaryMessageService;

    /**
     * 메시지 작성
     * POST /api/threads/{threadId}/messages
     */
    @PostMapping
    public ResponseEntity<DiaryMessageResponse> writeMessage(
            @AuthenticationPrincipal User sender,
            @PathVariable Long threadId,
            @RequestBody DiaryMessageRequest request) {

        DiaryMessage message = diaryMessageService.writeMessage(sender, threadId, request);
        DiaryMessageResponse response = DiaryMessageResponse.from(message);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * 특정 페이지 메시지 조회
     * GET /api/threads/{threadId}/messages?page=0
     */
    @GetMapping
    public ResponseEntity<DiaryMessagePageResponse> getMessagePage(
            @AuthenticationPrincipal User loginUser,
            @PathVariable Long threadId,
            @RequestParam(defaultValue = "0") int page) {

        DiaryMessagePageResponse response =
                diaryMessageService.getMessagePage(loginUser, threadId, page);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
