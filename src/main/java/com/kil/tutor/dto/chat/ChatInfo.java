package com.kil.tutor.dto.chat;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.kil.tutor.dto.chat.message.MessageInfo;
import lombok.Data;

import java.util.List;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = DirectInfo.class, name = "DIRECT"),
        @JsonSubTypes.Type(value = GroupInfo.class, name = "GROUP")
})
@Data
public class ChatInfo {
    private Long id;
    private Long unreadMessageCount;
    private MessageInfo lastMessage;
    private List<Long> participants;
}
