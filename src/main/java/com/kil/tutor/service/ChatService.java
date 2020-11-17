package com.kil.tutor.service;

import com.kil.tutor.entity.chat.Chat;
import com.kil.tutor.entity.chat.message.ChatMessage;
import com.kil.tutor.entity.chat.message.SimpleMessage;
import com.kil.tutor.entity.user.User;
import com.kil.tutor.repository.ChatRepository;
import com.kil.tutor.repository.MessageRepository;
import com.kil.tutor.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ChatService {
    //    private final EntityManager entityManager;
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;

    @Autowired
    public ChatService(
//            EntityManager entityManager,
            ChatRepository chatRepository,
            UserRepository userRepository,
            MessageRepository messageRepository
    ) {
//        this.entityManager = entityManager;
        this.chatRepository = chatRepository;
        this.userRepository = userRepository;
        this.messageRepository = messageRepository;
    }

    @Transactional(readOnly = true)
    public List<Chat> getChats(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            return Collections.emptyList();
        }
        List<Chat> chats = user.get().getChats();
        log.info("found {} chats for userId {}", chats.size(), userId);
        return chats;
    }

    @Transactional(readOnly = true)
    public List<User> getChatParticipants(Long chatId) {
        Optional<Chat> chat = chatRepository.findById(chatId);
        if (chat.isEmpty()) {
            return Collections.emptyList();
        }
        List<User> participants = chat.get().getParticipants();
        log.info("found {} participants for chatId {}", participants.size(), chatId);
        return participants;
    }

    @Transactional(readOnly = true)
    public List<ChatMessage> getMessages(Long chatId) {
        List<ChatMessage> messages = chatRepository.getOne(chatId).getMessages();
        Set<Long> authorIds = messages.stream()
                .map(ChatMessage::getAuthor)
                .map(User::getId)
                .collect(Collectors.toSet());
        log.info("found {} messages with authorIds {} for chatId {}", messages.size(), authorIds, chatId);
        return messages;
    }

    @Transactional
    public void saveMessage(Long userId, Long chatId, String text) {
        SimpleMessage message = new SimpleMessage();
        message.setMessageText(text);
        message.setDateTime(LocalDateTime.now());
        message.setAuthor(userRepository.getOne(userId));
        message.setChat(chatRepository.getOne(chatId));
        messageRepository.save(message);
    }

}
