package com.kil.tutor.entity.user;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Transient;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class Tutor extends User {
    public static final UserType TUTOR_TYPE = UserType.TUTOR;

    @Transient
    private UserType type = TUTOR_TYPE;

    private String status;

}
