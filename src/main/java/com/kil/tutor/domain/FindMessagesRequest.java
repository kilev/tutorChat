package com.kil.tutor.domain;

import lombok.Data;

@Data
public class FindMessagesRequest {
    private long chatId;
    private int page;
    private int pageSize;
}
