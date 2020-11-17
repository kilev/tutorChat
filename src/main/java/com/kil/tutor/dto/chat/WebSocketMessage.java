package com.kil.tutor.dto.chat;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class WebSocketMessage {
    private Long messageId;
    private Long userId;
    private Long chatId;
    private String text;
    private LocalDateTime dateTime;
}
