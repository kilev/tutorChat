package com.kil.tutor.entity.user;

import com.kil.tutor.domain.Role;
import com.kil.tutor.entity.BaseEntity;
import com.kil.tutor.entity.chat.Chat;
import com.kil.tutor.entity.chat.message.Reaction;
import com.talanlabs.avatargenerator.Avatar;
import com.talanlabs.avatargenerator.GitHubAvatar;
import com.talanlabs.avatargenerator.layers.backgrounds.RandomColorPaintBackgroundLayer;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Entity
@Table(name = "app_user")
@Inheritance(strategy = InheritanceType.JOINED)
public class User extends BaseEntity implements UserDetails {
    private static final Avatar AVATAR_GENERATOR = GitHubAvatar.newAvatarBuilder(900, 8)
            .layers(new RandomColorPaintBackgroundLayer())
            .build();

    @Column(unique = true)
    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.LAZY)
    @CollectionTable(name = "app_user_role", joinColumns = @JoinColumn(name = "app_user_id"))
    @Enumerated(value = EnumType.STRING)
    private List<Role> roles;

    @ManyToMany(mappedBy = "participants", fetch = FetchType.LAZY)
    private List<Chat> chats;

    //TODO add the ability to buy reactions (monetization)
    @ManyToMany
    private List<Reaction> availableReactions;

    @NotBlank
    private String lastName;

    private String middleName;

    @NotBlank
    private String firstName;

    @Lob
    private byte[] avatar;

    @CreationTimestamp
    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime lastOnlineDate;

    @PrePersist
    private void prePersist() {
        avatar = AVATAR_GENERATOR.createAsPngBytes(UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

