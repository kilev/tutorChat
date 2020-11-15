package com.kil.tutor.entity.chat.message;

import com.kil.tutor.entity.user.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String messageText;

    @NotNull
    @ManyToOne
    private User author;

    @ManyToMany
    private List<User> readUsers;

    @NotNull
    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime dateTime;
}
