package com.kil.tutor.entity.chat.message;

import com.kil.tutor.entity.BaseEntity;
import com.kil.tutor.entity.user.User;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
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
