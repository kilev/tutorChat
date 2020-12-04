package com.kil.tutor.dto.chat.vote;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class VotingRequest {
    @NotNull
    private Long authorId;

    @NotNull
    private Long chatId;

    @NotNull
    private Long voteId;

    @NotNull
    private Long optionId;
}
