package com.kil.tutor.dto.chat;

import com.kil.tutor.domain.ChatType;
import com.kil.tutor.entity.user.User;
import lombok.Data;

import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class ChatInfo {
    private String name;
    private ChatType type;
    private List<User> Participants;
}
