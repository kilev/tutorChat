package com.kil.tutor.entity.chat.message;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class SimpleMessage extends ChatMessage {
}
