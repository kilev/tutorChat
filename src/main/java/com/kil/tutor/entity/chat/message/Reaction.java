package com.kil.tutor.entity.chat.message;

import com.kil.tutor.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Entity
public class Reaction extends BaseEntity {
    @NotBlank
    private String name;
}
