package com.kil.tutor.entity.user;

import com.kil.tutor.domain.Role;
import com.kil.tutor.entity.chat.Chat;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;


@Data
@NoArgsConstructor
@Entity
@Table(name = "app_user")
@Inheritance(strategy = InheritanceType.JOINED)
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    @NotBlank
    private String lastName;

    private String middleName;

    @NotBlank
    private String firstName;

    private String avatarUrl;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime lastOnlineDate;

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

