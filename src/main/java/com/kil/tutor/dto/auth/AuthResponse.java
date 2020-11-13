package com.kil.tutor.dto.auth;

import lombok.Data;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class AuthResponse {
    private String authenticationToken;
    private Instant expiresAt;
    private String refreshToken;
    private List<String> roles;
    private LocalDateTime dateTime = LocalDateTime.now(Clock.systemUTC());
//            = LocalDateTime.now(ZoneId.of("Z"));
}
