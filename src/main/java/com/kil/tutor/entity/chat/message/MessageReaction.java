package com.kil.tutor.entity.chat.message;

import com.kil.tutor.entity.BaseEntity;
import com.kil.tutor.entity.user.User;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import java.util.List;

@Builder
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class MessageReaction extends BaseEntity {
    @ManyToOne
    private ChatMessage message;

    @ManyToOne
    private Reaction reaction;

    @ManyToMany
    private List<User> authors;
}
