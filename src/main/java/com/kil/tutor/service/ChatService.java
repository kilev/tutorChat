package com.kil.tutor.service;

import com.kil.tutor.domain.FindMessagesRequest;
import com.kil.tutor.entity.chat.Chat;
import com.kil.tutor.entity.chat.message.ChatMessage;
import com.kil.tutor.entity.chat.message.MessageReaction;
import com.kil.tutor.entity.chat.message.SimpleMessage;
import com.kil.tutor.entity.user.User;
import com.kil.tutor.repository.ChatRepository;
import com.kil.tutor.repository.MessageRepository;
import com.kil.tutor.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ChatService {
    private static final int DEFAULT_MESSAGE_PAGE_SIZE = 20;

    private final ChatRepository chatRepository;
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;

    @Autowired
    public ChatService(
            ChatRepository chatRepository,
            UserRepository userRepository,
            MessageRepository messageRepository
    ) {
        this.chatRepository = chatRepository;
        this.userRepository = userRepository;
        this.messageRepository = messageRepository;
    }

    @Transactional(readOnly = true)
    public List<Chat> getChats(Long userId) {
        User user = userRepository.getOne(userId);
        List<Chat> chats = user.getChats();
        log.info("found {} chats for userId {}", chats.size(), userId);
        return chats;
    }

    @Transactional(readOnly = true)
    public List<User> getChatParticipants(Long chatId) {
        Chat chat = chatRepository.getOne(chatId);
        List<User> participants = chat.getParticipants();
        log.info("found {} participants for chatId {}", participants.size(), chatId);
        return participants;
    }

    @Transactional(readOnly = true)
    public Page<ChatMessage> getMessages(FindMessagesRequest request) {
        int pageSize = request.getPageSize() == 0 ? DEFAULT_MESSAGE_PAGE_SIZE : request.getPageSize();
        Pageable pageRequest = PageRequest.of(request.getPage(), pageSize, Sort.by("dateTime").descending());

        ChatMessage exampleMessage = new ChatMessage();
        exampleMessage.setChat(chatRepository.getOne(request.getChatId()));
        Example<ChatMessage> messageExample = Example.of(exampleMessage);

        Page<ChatMessage> messages = messageRepository.findAll(messageExample, pageRequest);
        Set<Long> authorIds = messages.stream()
                .map(ChatMessage::getAuthor)
                .map(User::getId)
                .collect(Collectors.toSet());
        log.info("found {} messages with authorIds {} for chatId {}",
                messages.getTotalElements(), authorIds, request.getChatId());
        return messages;
    }

    @Transactional
    public ChatMessage saveMessage(Long userId, Long chatId, String text) {
        SimpleMessage message = new SimpleMessage();
        message.setMessageText(text);
        message.setAuthor(userRepository.getOne(userId));
        message.setChat(chatRepository.getOne(chatId));
        return messageRepository.save(message);
    }

    public List<MessageReaction> getReactions(Long messageId) {
        ChatMessage message = messageRepository.getOne(messageId);
        return message.getReactions();
    }

}
