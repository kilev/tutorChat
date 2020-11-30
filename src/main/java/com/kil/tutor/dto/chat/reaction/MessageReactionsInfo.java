package com.kil.tutor.dto.chat.reaction;

import lombok.Data;

import java.util.List;

@Data
public class MessageReactionsInfo {
    private Long chatId;
    private Long messageId;
    private List<ReactionInfo> reactions;
}
