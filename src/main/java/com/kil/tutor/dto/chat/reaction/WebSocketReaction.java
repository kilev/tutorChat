package com.kil.tutor.dto.chat.reaction;

import lombok.Data;

@Data
public class WebSocketReaction {
    private Long messageId;
    private Long reactionId;
}
