package com.kil.tutor.dto.chat.message;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class VoteInfo extends MessageInfo{
    private List<VoteOptionInfo> options;
}
