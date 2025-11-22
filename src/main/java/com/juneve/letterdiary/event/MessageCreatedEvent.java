package com.juneve.letterdiary.event;

public record MessageCreatedEvent(
        String threadTitle,
        String targetEmail,
        String previewContent
) {}
