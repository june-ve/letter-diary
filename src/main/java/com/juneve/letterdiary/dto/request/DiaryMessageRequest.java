package com.juneve.letterdiary.dto.request;

import lombok.Getter;

@Getter
public class DiaryMessageRequest {

    private Long threadId;
    private String content;
}
