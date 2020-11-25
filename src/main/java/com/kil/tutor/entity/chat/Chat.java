package com.kil.tutor.entity.chat;

import com.kil.tutor.entity.BaseEntity;
import com.kil.tutor.entity.chat.message.ChatMessage;
import com.kil.tutor.entity.user.User;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Chat extends BaseEntity {
    @OneToMany(mappedBy = "chat")
    private List<ChatMessage> messages;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<User> participants;

}
