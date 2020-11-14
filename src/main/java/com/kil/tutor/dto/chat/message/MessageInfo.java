package com.kil.tutor.dto.chat.message;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;

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
    private String messageText;
    private Long authorId;
}
