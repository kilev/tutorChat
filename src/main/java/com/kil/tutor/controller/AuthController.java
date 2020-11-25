package com.kil.tutor.controller;

import com.kil.tutor.domain.auth.UserAuth;
import com.kil.tutor.dto.auth.AuthResponse;
import com.kil.tutor.dto.auth.LoginRequest;
import com.kil.tutor.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

//TODO enable refresh token feature when clients support it.
@RestController
@RequestMapping(ApiConsts.AUTH)
public class AuthController {
    private final AuthService authService;
//    private final RefreshTokenService refreshTokenService;
    private final ServiceMapper mapper;

    @Autowired
    public AuthController(
            AuthService authService,
//            RefreshTokenService refreshTokenService,
            ServiceMapper mapper
    ) {
        this.authService = authService;
//        this.refreshTokenService = refreshTokenService;
        this.mapper = mapper;
    }

    @PostMapping(ApiConsts.LOGIN)
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        UserAuth auth = authService.auth(mapper.map(request));
        return mapper.map(auth);
    }

//    @PostMapping(ApiConsts.REFRESH)
//    private AuthResponse refresh(@Valid @RequestBody RefreshRequest request) {
//        UserAuth auth = authService.refreshAuth(mapper.map(request));
//        return mapper.map(auth);
//    }

//    @PostMapping(ApiConsts.LOGOUT)
//    private ResponseEntity<String> logout(@Valid @RequestBody LogoutRequest request) {
//        refreshTokenService.deleteByTokenValue(request.getRefreshToken());
//        return ResponseEntity.status(HttpStatus.OK).body("Logout successfully.");
//    }
}
