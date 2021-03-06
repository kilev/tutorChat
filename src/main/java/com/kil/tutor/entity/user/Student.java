package com.kil.tutor.entity.user;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.validation.constraints.NotEmpty;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class Student extends User{
    @NotEmpty
    private String groupName;
}
