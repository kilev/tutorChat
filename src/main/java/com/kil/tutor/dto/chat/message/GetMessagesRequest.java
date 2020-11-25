package com.kil.tutor.dto.chat.message;

import lombok.Data;

@Data
public class GetMessagesRequest {
    private Integer page;
    private Integer pageSize;
}
