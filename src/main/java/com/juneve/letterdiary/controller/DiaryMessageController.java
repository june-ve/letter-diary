package com.juneve.letterdiary.controller;

import com.juneve.letterdiary.dto.request.DiaryMessageRequest;
import com.juneve.letterdiary.dto.response.DiaryMessageResponse;
import com.juneve.letterdiary.entity.DiaryMessage;
import com.juneve.letterdiary.entity.User;
import com.juneve.letterdiary.service.DiaryMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/message")
public class DiaryMessageController {

    private final DiaryMessageService diaryMessageService;

    @PostMapping("/write")
    public ResponseEntity<DiaryMessageResponse> writeMessage(
            @AuthenticationPrincipal User sender,
            @RequestBody DiaryMessageRequest request) {

        DiaryMessage message = diaryMessageService.writeMessage(sender, request);
        DiaryMessageResponse response = DiaryMessageResponse.from(message);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
