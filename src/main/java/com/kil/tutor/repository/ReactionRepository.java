package com.kil.tutor.repository;

import com.kil.tutor.entity.chat.message.Reaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReactionRepository extends JpaRepository<Reaction, String> {
    @Query(value = "SELECT * FROM REACTION", nativeQuery = true)
    List<Reaction> getAllIds();
}
