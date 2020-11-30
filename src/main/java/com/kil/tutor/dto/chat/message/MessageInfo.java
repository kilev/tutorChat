package com.kil.tutor.dto.chat.message;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.kil.tutor.dto.chat.reaction.ReactionInfo;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = SimpleMessageInfo.class, name = "SIMPLE"),
        @JsonSubTypes.Type(value = VoteInfo.class, name = "VOTE")
})
@Data
public class MessageInfo {
    private Long id;
    private String text;
    private Long userId;
    private LocalDateTime dateTime;
    private List<ReactionInfo> reactions;
}
