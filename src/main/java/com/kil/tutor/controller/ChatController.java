package com.kil.tutor.controller;

import com.kil.tutor.dto.chat.ChatInfo;
import com.kil.tutor.dto.chat.GetChatResponse;
import com.kil.tutor.dto.chat.WebSocketMessage;
import com.kil.tutor.dto.chat.message.GetMessagesRequest;
import com.kil.tutor.dto.chat.message.GetMessagesResponse;
import com.kil.tutor.dto.chat.reaction.GetReactionsRequest;
import com.kil.tutor.dto.chat.reaction.GetReactionsResponse;
import com.kil.tutor.dto.chat.reaction.WebSocketReaction;
import com.kil.tutor.entity.chat.Chat;
import com.kil.tutor.entity.chat.message.ChatMessage;
import com.kil.tutor.entity.chat.message.MessageReaction;
import com.kil.tutor.entity.user.User;
import com.kil.tutor.service.ChatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
                .map(chat -> mapper.map(chat, 0L, null))
                .collect(Collectors.toList());
        return GetChatResponse.builder().chats(chatInfos).build();
    }

    @PostMapping("/{chatId}" + ApiConsts.MESSAGES)
    public GetMessagesResponse getMessages(@PathVariable Long chatId, @RequestBody GetMessagesRequest request) {
        Page<ChatMessage> messagePage = chatService.getMessages(mapper.map(chatId, request));
        return mapper.map(messagePage);
    }

    @PostMapping(ApiConsts.MESSAGE_REACTIONS)
    public GetReactionsResponse getReactions(@Valid @RequestBody GetReactionsRequest request) {
        List<MessageReaction> reactions = chatService.getReactions(request.getMessageId());
        return GetReactionsResponse.builder()
                .reactions(mapper.mapReactions(reactions))
                .build();
    }

    @MessageMapping("/chat/simpleMessage")
    public void message(@Payload WebSocketMessage message) {
        ChatMessage savedMessage = chatService.saveMessage(message.getUserId(), message.getChatId(), message.getText());
        message.setDateTime(savedMessage.getDateTime());
        message.setMessageId(savedMessage.getId());
        
        sendToChatByChatId(message.getChatId(), "/messages", message);
    }

    @MessageMapping("/chat/updateReaction")
    public void updateReaction(@Valid @Payload WebSocketReaction reaction) {
        MessageReaction updatedReaction = chatService.updateReaction(mapper.map(reaction));
        sendToChatByMessageId(updatedReaction.getMessage().getId(), "/reactions", mapper.map(updatedReaction));
    }

    @MessageExceptionHandler
    @SendToUser("/chat/error")
    public String handleException(Throwable exception) {
        log.error("WebSocketException: {}", Arrays.toString(exception.getStackTrace()));
        return exception.getMessage();
    }

    private void sendToChatByMessageId(Long messageId, String destination, Object message){
        sendToChatByChatId(chatService.getChatId(messageId), destination, message);
    }

    private void sendToChatByChatId(Long chatId, String destination, Object message) {
        List<User> chatParticipants = chatService.getChatParticipants(chatId);
        chatParticipants.forEach(user -> messagingTemplate
                .convertAndSendToUser(user.getId().toString(), destination, message));
    }

}
