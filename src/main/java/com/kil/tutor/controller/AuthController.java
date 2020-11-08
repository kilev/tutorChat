package com.kil.tutor.controller;

import com.kil.tutor.domain.auth.UserAuth;
import com.kil.tutor.dto.auth.*;
import com.kil.tutor.service.AuthService;
import com.kil.tutor.service.RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(ApiConsts.AUTH)
public class AuthController {
    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;
    private final ServiceMapper mapper;

    @Autowired
    public AuthController(
            AuthService authService,
            RefreshTokenService refreshTokenService,
            ServiceMapper mapper
    ) {
        this.authService = authService;
        this.refreshTokenService = refreshTokenService;
        this.mapper = mapper;
    }

    @PostMapping(ApiConsts.LOGIN)
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        UserAuth auth = authService.auth(mapper.convert(request));
        return mapper.convert(auth);
    }

    @PostMapping(ApiConsts.REFRESH)
    public AuthResponse refresh(@Valid @RequestBody RefreshRequest request) {
        UserAuth auth = authService.refreshAuth(mapper.convert(request));
        return mapper.convert(auth);
    }

    @PostMapping(ApiConsts.LOGOUT)
    public ResponseEntity<String> logout(@Valid @RequestBody LogoutRequest request) {
        refreshTokenService.deleteByTokenValue(request.getRefreshToken());
        return ResponseEntity.status(HttpStatus.OK).body("Logout successfully.");
    }
}
