package com.kil.tutor.entity.chat.message;

import com.kil.tutor.entity.BaseEntity;
import com.kil.tutor.entity.user.User;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotBlank;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Entity
public class VoteOption extends BaseEntity {

    public VoteOption(String optionText){
        this.optionText = optionText;
    }

    @NotBlank
    private String optionText;

    @ManyToMany
    private List<User> votedUsers;

}
