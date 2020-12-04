package com.kil.tutor.domain;

import lombok.Data;

@Data
public class Voting {
    private Long authorId;
    private Long chatId;
    private Long voteId;
    private Long optionId;
}
