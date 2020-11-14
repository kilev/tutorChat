package com.kil.tutor.dto.chat;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class GetChatResponse {
    private List<ChatInfo> chats;
}
