package com.kil.tutor.dto.user;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class TutorInfo extends UserInfo {
    private String status;
}
