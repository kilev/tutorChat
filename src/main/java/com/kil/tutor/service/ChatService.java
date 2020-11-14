package com.kil.tutor.service;

import com.kil.tutor.entity.chat.Chat;
import com.kil.tutor.entity.user.User;
import com.kil.tutor.repository.ChatRepository;
import com.kil.tutor.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ChatService {
    private final EntityManager entityManager;
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;

    @Autowired
    public ChatService(
            EntityManager entityManager,
            ChatRepository chatRepository,
            UserRepository userRepository
    ) {
        this.entityManager = entityManager;
        this.chatRepository = chatRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public List<Chat> getChats(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if(user.isEmpty()){
            return Collections.emptyList();
        }

        List<Chat> chats = user.get().getChats();
        return chats;
    }

//    public List<Chat> getChats(Long userId) {
//        Session session = entityManager.unwrap(Session.class);
//        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
//        CriteriaQuery<Chat> criteriaQuery = criteriaBuilder.createQuery(Chat.class);
//        Root<Chat> root = criteriaQuery.from(Chat.class);
//        criteriaQuery
//                .select(root)
//                .where(criteriaBuilder.like(root.get("")));
//
//        Query<Chat> query = session.createQuery(criteriaQuery);
//        List<Chat> results = query.getResultList();
//        return results;
//        return null;
//    }

}