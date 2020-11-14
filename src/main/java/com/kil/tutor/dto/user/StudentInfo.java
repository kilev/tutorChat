package com.kil.tutor.dto.user;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class StudentInfo extends UserInfo {
    private String groupName;
}
