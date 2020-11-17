package com.kil.tutor.entity.chat;

import com.kil.tutor.entity.chat.message.ChatMessage;
import com.kil.tutor.entity.user.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "chat")
    private List<ChatMessage> messages;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<User> participants;

}
