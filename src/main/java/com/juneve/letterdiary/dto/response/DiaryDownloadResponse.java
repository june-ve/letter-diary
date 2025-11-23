package com.juneve.letterdiary.dto.response;

public record DiaryDownloadResponse(
        String filename,
        String content
) {}
