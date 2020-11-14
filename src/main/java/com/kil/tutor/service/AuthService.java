package com.kil.tutor.service;

import com.google.common.collect.Lists;
import com.kil.tutor.domain.auth.UserAuth;
import com.kil.tutor.domain.auth.UserAuthRequest;
import com.kil.tutor.security.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;

@Service
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;
    private final Duration jwtExpirationDuration;

    @Autowired
    public AuthService(
            AuthenticationManager authenticationManager,
            JwtProvider jwtProvider,
            RefreshTokenService refreshTokenService,
            @Value("${security.jwt.expirationTime}") Duration jwtExpirationDuration
    ) {
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
        this.refreshTokenService = refreshTokenService;
        this.jwtExpirationDuration = jwtExpirationDuration;
    }

    public UserAuth auth(UserAuthRequest request) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        Instant jwtExpirationTime = getJwtExpireTime();
        String token = jwtProvider.generateToken(authentication, jwtExpirationTime);
//        String refreshToken = refreshTokenService.generate(request.getUsername()).getToken();
        return UserAuth.builder()
                .authenticationToken(token)
                .expiresAt(jwtExpirationTime)
//                .refreshToken(refreshToken)
                .authorities(Lists.newArrayList(authentication.getAuthorities()))
                .build();
    }

//    public UserAuth refreshAuth(UserAuthRefreshRequest request) {
//        RefreshToken oldRefreshToken = refreshTokenService.getRefreshToken(request.getRefreshToken());
//        refreshTokenService.delete(oldRefreshToken);
//
//        RefreshToken newRefreshToken = refreshTokenService.generate(oldRefreshToken.getUsername());
//
//        Instant jwtExpirationTime = getJwtExpireTime();
//        String token = jwtProvider.generateTokenWithUserName(oldRefreshToken.getUsername(), jwtExpirationTime);
//        return UserAuth.builder()
//                .authenticationToken(token)
//                .refreshToken(newRefreshToken.getToken())
//                .expiresAt(jwtExpirationTime)
//                .build();
//    }

    private Instant getJwtExpireTime() {
        return Instant.now().plus(jwtExpirationDuration);
    }
}
