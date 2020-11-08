package com.kil.tutor.dto.auth;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class LogoutRequest {
    @NotBlank
    private String refreshToken;
}
