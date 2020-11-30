package com.kil.tutor.entity.chat.message;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;

@Data
@NoArgsConstructor
@Entity
public class Reaction {
    @Id
    private String name;

    @Lob
    private byte[] icon;
}
