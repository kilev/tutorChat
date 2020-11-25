package com.kil.tutor.dto.chat.message;

import lombok.Data;

import java.util.List;

@Data
public class GetMessagesResponse {
    private List<MessageInfo> messages;
    private Integer TotalPages;
    private Long totalElements;
}
