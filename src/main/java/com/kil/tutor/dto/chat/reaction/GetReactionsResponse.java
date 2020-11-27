package com.kil.tutor.dto.chat.reaction;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class GetReactionsResponse {
    private List<ReactionInfo> reactions;

}
