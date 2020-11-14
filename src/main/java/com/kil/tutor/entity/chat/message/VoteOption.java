package com.kil.tutor.entity.chat.message;

import com.kil.tutor.entity.user.User;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@Entity
public class VoteOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String optionText;

    @ManyToMany
    private List<User> votedUsers;

}