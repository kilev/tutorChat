package com.kil.tutor.domain.auth;

import lombok.Data;

@Data
public class UserAuthRefreshRequest {
    private String refreshToken;
}
