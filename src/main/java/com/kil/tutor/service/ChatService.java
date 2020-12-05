package com.kil.tutor.service;

import com.kil.tutor.domain.FindMessagesRequest;
import com.kil.tutor.domain.MessageReactionUpdate;
import com.kil.tutor.domain.VoteRequest;
import com.kil.tutor.domain.Voting;
import com.kil.tutor.entity.chat.Chat;
import com.kil.tutor.entity.chat.message.*;
import com.kil.tutor.entity.user.User;
import com.kil.tutor.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ChatService {
    private static final int DEFAULT_MESSAGE_PAGE_SIZE = 20;
    private static final int MAX_MESSAGE_PAGE_SIZE = 100;

    private final ChatRepository chatRepository;
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    private final VoteRepository voteRepository;
    private final VoteOptionRepository voteOptionRepository;
    private final MessageReactionRepository messageReactionRepository;
    private final ReactionRepository reactionRepository;

    @Autowired
    public ChatService(
            ChatRepository chatRepository,
            UserRepository userRepository,
            MessageRepository messageRepository,
            VoteRepository voteRepository,
            VoteOptionRepository voteOptionRepository,
            MessageReactionRepository messageReactionRepository,
            ReactionRepository reactionRepository
    ) {
        this.chatRepository = chatRepository;
        this.userRepository = userRepository;
        this.messageRepository = messageRepository;
        this.voteRepository = voteRepository;
        this.voteOptionRepository = voteOptionRepository;
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
        int pageSize = request.getPageSize() == 0 ?
                DEFAULT_MESSAGE_PAGE_SIZE :
                Math.min(request.getPageSize(), MAX_MESSAGE_PAGE_SIZE);
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

        User reactionAuthor = userRepository.getOne(reactionAuthorId);
        List<MessageReaction> messageReactions = messageReactionRepository.findAllByMessageId(messageId);
        Optional<MessageReaction> existAuthorReaction = messageReactions.stream()
                .filter(messageReaction -> messageReaction.getAuthors().stream()
                        .anyMatch(author -> author.getId().equals(reactionAuthorId)))
                .findAny();

        if (existAuthorReaction.isPresent()) {
            MessageReaction existReaction = existAuthorReaction.get();
            existReaction.getAuthors().removeIf(author -> author.getId().equals(reactionAuthorId));

            if (!existReaction.getReaction().getName().equals(reactionName)) {
                MessageReaction updatedMessageReaction = getMessageReaction(messageId, reactionName);
                updatedMessageReaction.getAuthors().add(reactionAuthor);
            }

            if (existReaction.getAuthors().isEmpty()) {
                messageReactionRepository.delete(existReaction);
            }

        } else {
            MessageReaction messageReaction = getMessageReaction(messageId, reactionName);
            messageReaction.getAuthors().add(reactionAuthor);
        }

        messageReactionRepository.flush();
        return messageRepository.getOne(messageId);
    }

    private MessageReaction getMessageReaction(Long messageId, String reactionName) {
        return messageReactionRepository
                .findByMessageIdAndReactionName(messageId, reactionName)
                .orElseGet(() -> messageReactionRepository.save(MessageReaction.builder()
                        .message(messageRepository.getOne(messageId))
                        .reaction(reactionRepository.getOne(reactionName))
                        .authors(new ArrayList<>())
                        .build()));
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

    @Transactional
    public Vote createVote(VoteRequest voteRequest) {
        Vote vote = new Vote();
        vote.setMessageText(voteRequest.getTitle());
        vote.setAuthor(userRepository.getOne(voteRequest.getAuthorId()));
        vote.setChat(chatRepository.getOne(voteRequest.getChatId()));

        List<VoteOption> voteOptions = voteRequest.getOptions().stream()
                .map(VoteOption::new)
                .collect(Collectors.toList());
        vote.setOptions(voteOptions);

        voteOptionRepository.saveAll(voteOptions);
        return messageRepository.save(vote);
    }

    @Transactional
    public Vote voting(Voting voting) {
        Long voteId = voting.getVoteId();
        Long optionId = voting.getOptionId();
        Long authorId = voting.getAuthorId();

        if (!userRepository.existsById(authorId)) {
            throw new IllegalArgumentException("No user found with id: " + authorId);
        }

        Optional<Vote> chatVote = voteRepository.findById(voteId);
        Vote vote = chatVote.orElseThrow(() -> new IllegalArgumentException("No Vote found with id: " + voteId));

        Optional<VoteOption> votedOption = vote.getOptions().stream()
                .filter(option -> option.getVotedUsers().stream()
                        .anyMatch(votedUser -> votedUser.getId().equals(authorId)))
                .findAny();

        votedOption.ifPresent(oldOption -> oldOption.getVotedUsers().removeIf(user -> user.getId().equals(authorId)));

        if ((votedOption.isPresent() && !votedOption.get().getId().equals(optionId)) || votedOption.isEmpty()) {
            VoteOption targetOption = voteOptionRepository.findById(optionId)
                    .orElseThrow(() -> new IllegalArgumentException("No VoteOption found with id: " + optionId));
            targetOption.getVotedUsers().add(userRepository.getOne(authorId));
        }

        voteOptionRepository.flush();
        return voteRepository.getOne(voteId);
    }
}
