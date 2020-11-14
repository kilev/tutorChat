package com.kil.tutor.dto.chat;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class GroupInfo extends ChatInfo{
    private String name;
}
