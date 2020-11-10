package com.kil.tutor.controller;

import com.kil.tutor.domain.auth.UserAuth;
import com.kil.tutor.domain.auth.UserAuthRefreshRequest;
import com.kil.tutor.domain.auth.UserAuthRequest;
import com.kil.tutor.dto.auth.AuthResponse;
import com.kil.tutor.dto.auth.LoginRequest;
import com.kil.tutor.dto.auth.RefreshRequest;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public abstract class ServiceMapper {

    public abstract UserAuthRequest convert(LoginRequest request);

    public abstract UserAuthRefreshRequest convert(RefreshRequest request);

    public abstract AuthResponse convert(UserAuth userAuth);

    protected List<String> convert(Collection<? extends GrantedAuthority> authorities){
        return authorities.stream()
                .map(this::convert)
                .collect(Collectors.toList());
    }

    private String convert(GrantedAuthority authority) {
        return authority.getAuthority();
    }
}
