package com.kil.tutor.domain.auth;

import lombok.Data;

@Data
public class UserAuthRequest {
    private String username;
    private String password;
}
