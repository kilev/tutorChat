package com.kil.tutor.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.Instant;

@Data
@Entity
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String token;
    private String username;
    private Instant createdDate;
}

