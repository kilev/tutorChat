package com.kil.tutor.entity.chat;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class Vote extends ChatMessage {

    private List<VoteOption> options;
}
