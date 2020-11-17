package com.kil.tutor.dto.chat;

import lombok.Data;

@Data
public class WebSocketMessage {
    private Long userId;
    private Long chatId;
    private String text;
}
