package com.kil.tutor.domain.auth;

import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.time.Instant;
import java.util.Collection;

@Data
@Builder
public class UserAuth {
    private String authenticationToken;
    private Instant expiresAt;
    private String refreshToken;
    private Collection<? extends GrantedAuthority> roles;
}
