package com.kil.tutor.dto.chat;

import com.kil.tutor.entity.user.User;
import lombok.Data;

import java.util.List;

@Data
public class ChatParticipants {
    private List<User> participants;
}
