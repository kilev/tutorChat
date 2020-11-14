package com.kil.tutor.dto.chat.message;

import lombok.Data;

import java.util.List;

@Data
public class VoteOptionInfo {
    private Long id;
    private String optionText;
    private List<Long> votedUserIds;
}
