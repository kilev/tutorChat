package com.kil.tutor.dto.chat.vote;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class CreateVoteRequest {
    @NotNull
    private Long authorId;

    @NotNull
    private Long chatId;

    @NotBlank
    private String title;

    @NotNull
    private List<@NotBlank String> options;
}
