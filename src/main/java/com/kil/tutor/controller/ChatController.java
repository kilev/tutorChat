package com.kil.tutor.controller;

import com.kil.tutor.dto.chat.ChatInfo;
import com.kil.tutor.dto.chat.GetChatResponse;
import com.kil.tutor.dto.chat.WebSocketMessage;
import com.kil.tutor.dto.chat.message.GetChatMessagesResponse;
import com.kil.tutor.entity.chat.Chat;
import com.kil.tutor.entity.chat.message.ChatMessage;
import com.kil.tutor.entity.user.User;
import com.kil.tutor.service.ChatService;
import com.kil.tutor.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequestMapping(ApiConsts.CHAT)
@RestController
public class ChatController {
    private final SimpMessagingTemplate messagingTemplate;
    private final UserService userService;
    private final ChatService chatService;
    private final ServiceMapper mapper;

    @Autowired
    public ChatController(
            SimpMessagingTemplate messagingTemplate,
            UserService userService,
            ChatService chatService,
            ServiceMapper mapper
    ) {
        this.messagingTemplate = messagingTemplate;
        this.userService = userService;
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

    @GetMapping("/{id}" + ApiConsts.MESSAGES)
    public GetChatMessagesResponse getMessages(@PathVariable Long id) {
        List<ChatMessage> messages = chatService.getMessages(id);

        GetChatMessagesResponse response = new GetChatMessagesResponse();
        response.setMessages(mapper.map(messages));
        return response;
    }

    @MessageMapping("/chat")
    public void message(@Payload WebSocketMessage message) {
        ChatMessage savedMessage = chatService.saveMessage(message.getUserId(), message.getChatId(), message.getText());
        message.setDateTime(savedMessage.getDateTime());
        message.setMessageId(savedMessage.getId());

        List<User> chatParticipants = chatService.getChatParticipants(message.getChatId());
        chatParticipants.forEach(user -> messagingTemplate
                .convertAndSendToUser(user.getId().toString(), "/messages", message));

    }

    @MessageExceptionHandler
    @SendToUser("/chat/error")
    public String handleException(Throwable exception) {
        log.error("WebSockerException: {}", Arrays.toString(exception.getStackTrace()));
        return exception.getMessage();
    }

//    @EventListener
//    public void handleWebSocketDisconnectedEvent(SessionDisconnectEvent event) {
//        String userUnfo = event.getUser() == null ? "" : "user = " + event.getUser();
//        log.info(userUnfo + "was disconnected, last online time: " + LocalDateTime.now());
////        StompCommand
//    }

    public class FilterChannelInterceptor implements ChannelInterceptor {
        @Override
        public Message<?> preSend(Message<?> message, MessageChannel channel) {
            StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(message);
            if (StompCommand.SUBSCRIBE.equals(headerAccessor.getCommand())) {
                // Your logic
            }
            return message;
        }
    }

//    @MessageMapping("/chat/{chatId}")
//    public void sendMessage(@DestinationVariable String chatId, WebSocketMessage message) {
//        System.out.println("handling send message: " + message + " to: " + chatId);
//        boolean isExists = UserStorage.getInstance().getUsers().contains(chatId);
//        if (isExists) {
//            messagingTemplate.convertAndSend("/topic/messages/" + chatId, message);
//        }
//    }
}
