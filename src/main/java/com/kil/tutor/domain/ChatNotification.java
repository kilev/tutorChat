package com.kil.tutor.domain;

import lombok.Data;

@Data
public class ChatNotification {
    private Long chatId;
    private String chatName;
    private Long senderId;
    private String senderLastName;
    private String senderMiddleName;
    private String senderFirstName;
}
