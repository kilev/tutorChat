package com.kil.tutor.domain;

import lombok.Data;

@Data
public class MessageReactionInfo {
    private Long authorId;
    private Long messageId;
    private Long reactionId;
}
