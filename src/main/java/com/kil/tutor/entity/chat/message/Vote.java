package com.kil.tutor.entity.chat.message;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class Vote extends ChatMessage {

    @OneToMany
    private List<VoteOption> options;
}
