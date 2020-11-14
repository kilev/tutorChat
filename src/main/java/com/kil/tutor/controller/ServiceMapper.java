package com.kil.tutor.controller;

import com.kil.tutor.domain.auth.UserAuth;
import com.kil.tutor.domain.auth.UserAuthRefreshRequest;
import com.kil.tutor.domain.auth.UserAuthRequest;
import com.kil.tutor.dto.auth.AuthResponse;
import com.kil.tutor.dto.auth.LoginRequest;
import com.kil.tutor.dto.auth.RefreshRequest;
import com.kil.tutor.dto.chat.ChatInfo;
import com.kil.tutor.dto.chat.DirectInfo;
import com.kil.tutor.dto.chat.GroupInfo;
import com.kil.tutor.dto.chat.message.MessageInfo;
import com.kil.tutor.dto.user.StudentInfo;
import com.kil.tutor.dto.user.TutorInfo;
import com.kil.tutor.dto.user.UserInfo;
import com.kil.tutor.entity.chat.Chat;
import com.kil.tutor.entity.chat.Direct;
import com.kil.tutor.entity.chat.Group;
import com.kil.tutor.entity.user.Student;
import com.kil.tutor.entity.user.Tutor;
import com.kil.tutor.entity.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public abstract class ServiceMapper {

    public abstract UserAuthRequest map(LoginRequest request);

    public abstract UserAuthRefreshRequest map(RefreshRequest request);

    @Mapping(target = "roles", source = "authorities")
    public abstract AuthResponse map(UserAuth userAuth);

    protected String map(GrantedAuthority authority) {
        return authority.getAuthority();
    }

    public abstract List<UserInfo> mapUsers(List<User> users);

    protected UserInfo map(User user) {
        if (user instanceof Tutor) {
            return this.map((Tutor) user);
        } else if (user instanceof Student) {
            return this.map((Student) user);
        } else throw new UnsupportedOperationException();
    }

    protected abstract TutorInfo map(Tutor tutor);

    protected abstract StudentInfo map(Student student);

//    public abstract List<ChatInfo> mapChats(List<Chat> chats);

    public ChatInfo map(Chat chat, Long unreadMessageCount, MessageInfo lastMessage) {
        if (chat instanceof Direct) {
            return this.map((Direct) chat, unreadMessageCount, lastMessage);
        } else if (chat instanceof Group) {
            return this.map((Group) chat, unreadMessageCount, lastMessage);
        } else throw new UnsupportedOperationException();
    }

    @Mapping(target = "id", source = "direct.id")
    protected abstract DirectInfo map(Direct direct, Long unreadMessageCount, MessageInfo lastMessage);

    @Mapping(target = "id", source = "group.id")
    protected abstract GroupInfo map(Group group, Long unreadMessageCount, MessageInfo lastMessage);

    protected Long mapToUserId(User user) {
        return user.getId();
    }
}
