package com.kil.tutor.dto.user;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class GetUsersResponse {
    private List<UserInfo> users;
}
