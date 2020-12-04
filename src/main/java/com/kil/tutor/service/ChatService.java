package com.kil.tutor.service;

import com.kil.tutor.domain.FindMessagesRequest;
import com.kil.tutor.domain.MessageReactionUpdate;
import com.kil.tutor.entity.chat.Chat;
import com.kil.tutor.entity.chat.message.ChatMessage;
import com.kil.tutor.entity.chat.message.MessageReaction;
import com.kil.tutor.entity.chat.message.Reaction;
import com.kil.tutor.entity.chat.message.SimpleMessage;
import com.kil.tutor.entity.user.User;
import com.kil.tutor.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ChatService {
    private static final int DEFAULT_MESSAGE_PAGE_SIZE = 20;
    private static final int MAX_MESSAGE_PAGE_SIZE = 100;

    ExampleMatcher AUTHOR_EXIST_REACTION_MATCHER = ExampleMatcher.matchingAll()
            .withMatcher("authors", ExampleMatcher.GenericPropertyMatchers.contains());

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
        log.info("found {} messages for chatId {}", messages.getTotalElements(), request.getChatId());
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

    @Transactional
    public ChatMessage updateMessageReaction(MessageReactionUpdate reactionUpdateInfo) {
        Long reactionAuthorId = reactionUpdateInfo.getAuthorId();
        Long messageId = reactionUpdateInfo.getMessageId();
        String reactionName = reactionUpdateInfo.getReactionName().toUpperCase();

        if (!messageRepository.existsById(messageId)) {
            throw new IllegalArgumentException("No Message found with id: " + messageId);
        }
        if (!reactionRepository.existsById(reactionName)) {
            throw new IllegalArgumentException("No Reaction found with name: " + reactionName);
        }
        if (!userRepository.existsById(reactionAuthorId)) {
            throw new IllegalArgumentException("No user found with id: " + reactionAuthorId);
        }

//        ChatMessage message = messageRepository.getOne(messageId);

        User reactionAuthor = userRepository.getOne(reactionAuthorId);
        MessageReaction exampleReaction = MessageReaction.builder()
                .message(messageRepository.getOne(messageId))
                .authors(Collections.singletonList(reactionAuthor))
                .build();
        Optional<MessageReaction> existAuthorReaction = messageReactionRepository
                .findOne(Example.of(exampleReaction, AUTHOR_EXIST_REACTION_MATCHER));

        if (existAuthorReaction.isPresent()) {
            MessageReaction existReaction = existAuthorReaction.get();
            existReaction.getAuthors().remove(reactionAuthor);

            if (!existReaction.getReaction().getName().equals(reactionName)) {
                MessageReaction updatedMessageReaction = getMessageReaction(messageId, reactionName);
                updatedMessageReaction.getAuthors().add(reactionAuthor);
            }

            if (existReaction.getAuthors().isEmpty()) {
                messageReactionRepository.delete(exampleReaction);
            }

        } else {
            MessageReaction messageReaction = getMessageReaction(messageId, reactionName);
            messageReaction.getAuthors().add(reactionAuthor);
            messageReactionRepository.save(messageReaction);
        }

        return messageRepository.getOne(messageId);
    }

    private MessageReaction getMessageReaction(Long messageId, String reactionName) {
        ChatMessage message = messageRepository.getOne(messageId);
        Reaction reaction = reactionRepository.getOne(reactionName);

        MessageReaction requiredMessageReaction = MessageReaction.builder()
                .message(message)
                .reaction(reaction)
                .build();
        Optional<MessageReaction> messageReaction = messageReactionRepository
                .findOne(Example.of(requiredMessageReaction));

        return messageReaction.orElse(MessageReaction.builder()
                .message(message)
                .reaction(reaction)
                .authors(new ArrayList<>())
                .build());
    }

    @Transactional(readOnly = true)
    public byte[] getReactionImage(String reactionId) {
        Optional<Reaction> reaction = reactionRepository.findById(reactionId.toUpperCase());
        return reaction.map(Reaction::getIcon)
                .orElseThrow(() -> new IllegalArgumentException("IconNotFoundException: " + reactionId));
    }

    public List<String> getAllIconIds() {
        return reactionRepository.getAllIds().stream()
                .map(Reaction::getName)
                .collect(Collectors.toList());
    }
}
