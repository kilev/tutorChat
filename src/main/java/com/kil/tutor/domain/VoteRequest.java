package com.kil.tutor.domain;

import lombok.Data;

import java.util.List;

@Data
public class VoteRequest {
    private Long authorId;
    private Long chatId;
    private String title;
    private List<String> options;
}
