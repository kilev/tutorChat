package com.kil.tutor.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Instant;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Entity
public class RefreshToken extends BaseEntity{
    @NotBlank
    private String token;

    @NotBlank
    private String username;

    @NotNull
    private Instant createdDate;
}

