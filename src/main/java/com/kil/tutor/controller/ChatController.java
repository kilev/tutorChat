package com.kil.tutor.controller;

import com.kil.tutor.dto.chat.ChatInfo;
import com.kil.tutor.dto.chat.GetChatResponse;
import com.kil.tutor.dto.chat.GetIconIdsResponse;
import com.kil.tutor.dto.chat.WebSocketMessage;
import com.kil.tutor.dto.chat.message.GetMessagesRequest;
import com.kil.tutor.dto.chat.message.GetMessagesResponse;
import com.kil.tutor.dto.chat.message.VoteInfo;
import com.kil.tutor.dto.chat.reaction.MessageReactionsInfo;
import com.kil.tutor.dto.chat.reaction.WebSocketReaction;
import com.kil.tutor.dto.chat.vote.CreateVoteRequest;
import com.kil.tutor.dto.chat.vote.VotingRequest;
import com.kil.tutor.entity.chat.Chat;
import com.kil.tutor.entity.chat.message.ChatMessage;
import com.kil.tutor.entity.chat.message.Vote;
import com.kil.tutor.entity.user.User;
import com.kil.tutor.service.ChatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequestMapping(ApiConsts.CHAT)
@RestController
public class ChatController {
    private final SimpMessagingTemplate messagingTemplate;
    private final ChatService chatService;
    private final ServiceMapper mapper;

    @Autowired
    public ChatController(
            SimpMessagingTemplate messagingTemplate,
            ChatService chatService,
            ServiceMapper mapper
    ) {
        this.messagingTemplate = messagingTemplate;
        this.chatService = chatService;
        this.mapper = mapper;
    }

    @GetMapping(ApiConsts.ALL)
    public GetChatResponse getChats(@ApiIgnore @AuthenticationPrincipal User user) {
        List<Chat> chats = chatService.getChats(user.getId());
        List<ChatInfo> chatInfos = chats.stream()
                .map(chat -> mapper.map(chat, user.getId(), 0L, null))
                .collect(Collectors.toList());
        return GetChatResponse.builder().chats(chatInfos).build();
    }

    @Transactional(readOnly = true)
    @PostMapping("/{chatId}" + ApiConsts.MESSAGES)
    public GetMessagesResponse getMessages(@PathVariable Long chatId, @RequestBody GetMessagesRequest request) {
        Page<ChatMessage> messagePage = chatService.getMessages(mapper.map(chatId, request));
        return mapper.map(messagePage);
    }

    @MessageMapping("/chat/simpleMessage")
    public void message(@Payload WebSocketMessage message) {
        ChatMessage savedMessage = chatService.saveMessage(message.getUserId(), message.getChatId(), message.getText());
        message.setDateTime(savedMessage.getDateTime());
        message.setMessageId(savedMessage.getId());
        sendToChatByChatId(message.getChatId(), "/messages", message);
    }

    @Transactional
    @MessageMapping("/chat/updateReaction")
    public void updateReaction(@Valid @Payload WebSocketReaction reaction) {
        ChatMessage message = chatService.updateMessageReaction(mapper.map(reaction));
        MessageReactionsInfo messageReactionsInfo = mapper.mapToMessageReactionInfo(
                message.getId(),
                reaction.getChatId(),
                message.getReactions());
        sendToChatByChatId(reaction.getChatId(), "/reactions", messageReactionsInfo);
    }

    @MessageMapping("chat/vote/create")
    public void createVote(@Valid @Payload CreateVoteRequest request) {
        Vote vote = chatService.createVote(mapper.map(request));
        VoteInfo voteInfo = mapper.map(vote, request.getChatId());
        sendToChatByChatId(request.getChatId(), "/votes", voteInfo);
    }

    @Transactional
    @MessageMapping("chat/vote/voting")
    public void voting(@Valid @Payload VotingRequest request) {
        Vote vote = chatService.voting(mapper.map(request));
        VoteInfo voteInfo = mapper.map(vote, request.getChatId());
        sendToChatByChatId(request.getChatId(), "/voting", voteInfo);
    }

    @MessageExceptionHandler
    @SendToUser("/error")
    public String handleException(Throwable exception) {
        log.error("WebSocketException: {}", Arrays.toString(exception.getStackTrace()));
        return exception.getMessage();
    }

    private void sendToChatByChatId(Long chatId, String destination, Object message) {
        List<User> chatParticipants = chatService.getChatParticipants(chatId);
        chatParticipants.forEach(user -> messagingTemplate
                .convertAndSendToUser(user.getId().toString(), destination, message));
    }

    @GetMapping(value = ApiConsts.REACTIONS + "/{reactionName}", produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] getReactionImage(@PathVariable String reactionName) {
        log.info("icon request!");
        return chatService.getReactionImage(reactionName);
    }

    @GetMapping(ApiConsts.REACTION_ICONS)
    public GetIconIdsResponse getIconIds() {
        GetIconIdsResponse response = new GetIconIdsResponse();
        response.setAllIconIds(chatService.getAllIconIds());
        return response;
    }

}
