package com.kil.tutor.repository;

import com.kil.tutor.entity.chat.message.MessageReaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageReactionRepository extends JpaRepository<MessageReaction, Long> {
}
