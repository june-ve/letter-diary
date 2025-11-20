package com.juneve.letterdiary.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class MessageCreatedEvent extends ApplicationEvent {

    private final String threadTitle;
    private final String targetEmail;
    private final String previewContent;

    public MessageCreatedEvent(String threadTitle, String targetEmail, String previewContent) {
        super(threadTitle);
        this.threadTitle = threadTitle;
        this.targetEmail = targetEmail;
        this.previewContent = previewContent;
    }
}
