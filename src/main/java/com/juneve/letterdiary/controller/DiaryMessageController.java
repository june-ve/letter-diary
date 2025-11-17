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
@RequestMapping("/api/thread/{threadId}/messages")
public class DiaryMessageController {

    private final DiaryMessageService diaryMessageService;

    @PostMapping
    public ResponseEntity<DiaryMessageResponse> writeMessage(
            @AuthenticationPrincipal User sender,
            @PathVariable Long threadId,
            @RequestBody DiaryMessageRequest request) {

        DiaryMessage message = diaryMessageService.writeMessage(sender, threadId, request);
        DiaryMessageResponse response = DiaryMessageResponse.from(message);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

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
