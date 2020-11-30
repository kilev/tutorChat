package com.kil.tutor.domain;

import lombok.Data;

@Data
public class MessageReactionUpdate {
    private Long authorId;
    private Long messageId;
    private String reactionName;
}
