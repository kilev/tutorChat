package com.kil.tutor.dto.user;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;

import java.time.LocalDateTime;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = StudentInfo.class, name = "STUDENT"),
        @JsonSubTypes.Type(value = TutorInfo.class, name = "TUTOR")
})
@Data
public class UserInfo {
    private Long id;
    private String lastName;
    private String middleName;
    private String firstName;
    private LocalDateTime lastOnlineDate;
}
