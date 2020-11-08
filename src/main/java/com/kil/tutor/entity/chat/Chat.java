package com.kil.tutor.entity.chat;

import com.kil.tutor.domain.ChatType;
import com.kil.tutor.entity.user.User;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Entity
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    private String name;

    @NotNull
    private ChatType type;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<User> Participants;

}
