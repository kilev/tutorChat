package com.kil.tutor.domain;

import com.kil.tutor.dto.chat.message.MessageInfo;
import com.kil.tutor.entity.user.User;
import lombok.Data;

import java.util.List;

@Data
public class UserChatInfo {
    private Long id;
    private String name;
    private Long unreadMessageCount;
    private MessageInfo lastMessage;
    private List<User> participants;
}
