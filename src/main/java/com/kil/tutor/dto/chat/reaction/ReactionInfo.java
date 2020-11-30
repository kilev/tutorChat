package com.kil.tutor.dto.chat.reaction;

import lombok.Data;

import java.util.List;

@Data
public class ReactionInfo {
    private String name;
    private List<Long> authorIds;
}
