package com.kil.tutor.dto.chat;

import lombok.Data;

import java.util.List;

@Data
public class GetUsersRequest {
    private List<Long> userIds;
}
