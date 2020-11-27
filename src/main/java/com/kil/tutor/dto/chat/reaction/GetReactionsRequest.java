package com.kil.tutor.dto.chat.reaction;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class GetReactionsRequest {
    @NotNull
    Long messageId;
}
