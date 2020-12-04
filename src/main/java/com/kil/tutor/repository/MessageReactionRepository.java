package com.kil.tutor.repository;

import com.kil.tutor.entity.chat.message.MessageReaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MessageReactionRepository extends JpaRepository<MessageReaction, Long> {
    List<MessageReaction> findAllByMessageId(Long messageId);

    Optional<MessageReaction> findByMessageIdAndReactionName(Long messageId, String reactionId);
}
