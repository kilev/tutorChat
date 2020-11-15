package com.kil.tutor.dto.chat;

import lombok.Data;

@Data
public class WebSocketMessage {
    private String from;
    private String text;
}
