package com.kil.tutor.dto.chat.message;

import lombok.Data;

import java.util.List;

@Data
public class GetChatMessagesResponse {
    private List<MessageInfo> messages;
}
