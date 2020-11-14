package com.kil.tutor.entity.chat;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.validation.constraints.NotEmpty;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class Group extends Chat{

    @NotEmpty
    private String name;
}
