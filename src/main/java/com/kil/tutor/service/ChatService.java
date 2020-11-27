package com.kil.tutor.service;

import com.google.common.collect.Collections2;
import com.kil.tutor.domain.FindMessagesRequest;
import com.kil.tutor.domain.MessageReactionInfo;
import com.kil.tutor.entity.chat.Chat;
import com.kil.tutor.entity.chat.message.ChatMessage;
import com.kil.tutor.entity.chat.message.MessageReaction;
import com.kil.tutor.entity.chat.message.SimpleMessage;
import com.kil.tutor.entity.user.User;
import com.kil.tutor.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ChatService {
    private static final int DEFAULT_MESSAGE_PAGE_SIZE = 20;

    private final ChatRepository chatRepository;
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    private final MessageReactionRepository messageReactionRepository;
    private final ReactionRepository reactionRepository;

    @Autowired
    public ChatService(
            ChatRepository chatRepository,
            UserRepository userRepository,
            MessageRepository messageRepository,
            MessageReactionRepository messageReactionRepository,
            ReactionRepository reactionRepository
    ) {
        this.chatRepository = chatRepository;
        this.userRepository = userRepository;
        this.messageRepository = messageRepository;
        this.messageReactionRepository = messageReactionRepository;
        this.reactionRepository = reactionRepository;
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

    @Transactional(readOnly = true)
    public List<MessageReaction> getReactions(Long messageId) {
        ChatMessage message = messageRepository.getOne(messageId);
        List<MessageReaction> reactions = message.getReactions();
        Set<Long> authorIds = reactions.stream()
                .flatMap(reaction -> reaction.getAuthors().stream())
                .map(User::getId)
                .collect(Collectors.toSet());

        log.info("found {} reactions for message {}, with total authors {}", reactions.size(), messageId, authorIds.size());
        return reactions;
    }

    @Transactional
    public MessageReaction updateReaction(MessageReactionInfo reactionInfo) {
        Long messageId = reactionInfo.getMessageId();
        Long reactionId = reactionInfo.getReactionId();

        MessageReaction exampleReaction = MessageReaction.builder()
                .message(messageRepository.getOne(messageId))
                .reaction(reactionRepository.getOne(reactionId))
                .build();
        Example<MessageReaction> reactionExample = Example.of(exampleReaction);
        Optional<MessageReaction> existReaction = messageReactionRepository.findOne(reactionExample);

        MessageReaction reaction;
        if (existReaction.isPresent()) {
            log.info("reaction {} exists for message {}", reactionId, messageId);

            reaction = existReaction.get();
            Collection<Long> authorIds = Collections2.transform(reaction.getAuthors(), User::getId);

            Long authorId = reactionInfo.getAuthorId();
            if (authorIds.contains(authorId)) {
                authorIds.remove(authorId);
            } else {
                authorIds.add(authorId);
            }
        } else {
            log.info("reaction {} not exists for message {}. Creating new", reactionId, messageId);
            reaction = MessageReaction.builder()
                    .message(messageRepository.getOne(messageId))
                    .reaction(reactionRepository.getOne(reactionId))
                    .authors(Collections.singletonList(userRepository.getOne(reactionInfo.getAuthorId())))
                    .build();
        }

        return messageReactionRepository.save(reaction);
    }

    @Transactional(readOnly = true)
    public Long getChatId(Long messageId){
        ChatMessage chat = messageRepository.getOne(messageId);
        return chat.getId();
    }

}
