package com.kil.tutor.entity.chat;

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

//    @OneToMany
//    private List<ChatMessage> messages;

    @JoinTable(
            name = "Chat_participants",
            joinColumns = { @JoinColumn(name = "participantsS_id") },
            inverseJoinColumns = { @JoinColumn(name = "chatT_id") }
    )
    @ManyToMany
    private List<User> participants;

}
