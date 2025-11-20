package com.juneve.letterdiary.event;

import com.juneve.letterdiary.entity.DiaryMessage;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class MessageCreatedEvent extends ApplicationEvent {

    private final DiaryMessage message;

    public MessageCreatedEvent(DiaryMessage message) {
        super(message);
        this.message = message;
    }
}
