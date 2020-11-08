package com.kil.tutor.controller;

import com.kil.tutor.entity.user.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatController {

    public

    @GetMapping
    public void test(@AuthenticationPrincipal User user) {

    }
}
