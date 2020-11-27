package com.kil.tutor.dto.chat.reaction;

import lombok.Data;

import java.util.List;

@Data
public class ReactionInfo {
    private Long reactionId;
    private List<Long> authorIds;
}
