package com.kil.tutor.entity.chat.message;

import com.kil.tutor.entity.BaseEntity;
import com.kil.tutor.entity.chat.Chat;
import com.kil.tutor.entity.user.User;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class ChatMessage extends BaseEntity {
    @NotBlank
    private String messageText;

    @NotNull
    @ManyToOne
    private User author;

    @CreationTimestamp
    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime dateTime;

    @ManyToOne
    private Chat chat;

    @OneToMany(mappedBy = "message")
    private List<MessageReaction> reactions;
}
