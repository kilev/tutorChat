package com.kil.tutor.dto.chat.reaction;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class WebSocketReaction {
    @NotNull
    private Long authorId;

    @NotNull
    private Long chatId;

    @NotNull
    private Long messageId;

    @NotNull
    private String reactionName;
}
