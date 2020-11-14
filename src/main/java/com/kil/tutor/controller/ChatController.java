package com.kil.tutor.controller;

import com.kil.tutor.dto.chat.GetChatResponse;
import com.kil.tutor.entity.chat.Chat;
import com.kil.tutor.entity.user.User;
import com.kil.tutor.service.ChatService;
import com.kil.tutor.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@RequestMapping(ApiConsts.CHAT)
@RestController
public class ChatController {
    private final UserService userService;
    private final ChatService chatService;
    private final ServiceMapper mapper;

    @Autowired
    public ChatController(
            UserService userService,
            ChatService chatService,
            ServiceMapper mapper
    ) {
        this.userService = userService;
        this.chatService = chatService;
        this.mapper = mapper;
    }

    @GetMapping("test")
    public ResponseEntity<String> test(@AuthenticationPrincipal User user) {
        String username = user == null? "" : user.getUsername();
        return ResponseEntity.ok().body("test passed successful!" + username);
    }

    @GetMapping(ApiConsts.ALL)
    public GetChatResponse getChats(@ApiIgnore @AuthenticationPrincipal User user) {
//        List<Chat> chats = chatService.getChats(user.getId());

//        return chats;
//        List<Chat> chats = user.getChats();
//        List<ChatInfo> chatInfos = chats.stream()
//                .map(chat -> mapper.map(chat, 0L, null))
//                .collect(Collectors.toList());
//        return GetChatResponse.builder().chats(chatInfos).build();

        List<Chat> chats = chatService.getChats(user.getId());
        return null;
    }

//    public GetChatParticipants

}
