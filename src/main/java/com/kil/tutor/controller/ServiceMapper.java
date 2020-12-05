package com.kil.tutor.controller;

import com.kil.tutor.domain.FindMessagesRequest;
import com.kil.tutor.domain.MessageReactionUpdate;
import com.kil.tutor.domain.VoteRequest;
import com.kil.tutor.domain.Voting;
import com.kil.tutor.domain.auth.UserAuth;
import com.kil.tutor.domain.auth.UserAuthRefreshRequest;
import com.kil.tutor.domain.auth.UserAuthRequest;
import com.kil.tutor.dto.auth.AuthResponse;
import com.kil.tutor.dto.auth.LoginRequest;
import com.kil.tutor.dto.auth.RefreshRequest;
import com.kil.tutor.dto.chat.ChatInfo;
import com.kil.tutor.dto.chat.DirectInfo;
import com.kil.tutor.dto.chat.GroupInfo;
import com.kil.tutor.dto.chat.message.*;
import com.kil.tutor.dto.chat.reaction.MessageReactionsInfo;
import com.kil.tutor.dto.chat.reaction.ReactionInfo;
import com.kil.tutor.dto.chat.reaction.WebSocketReaction;
import com.kil.tutor.dto.chat.vote.CreateVoteRequest;
import com.kil.tutor.dto.chat.vote.VotingRequest;
import com.kil.tutor.dto.user.StudentInfo;
import com.kil.tutor.dto.user.TutorInfo;
import com.kil.tutor.dto.user.UserInfo;
import com.kil.tutor.entity.chat.Chat;
import com.kil.tutor.entity.chat.DirectChat;
import com.kil.tutor.entity.chat.GroupChat;
import com.kil.tutor.entity.chat.message.*;
import com.kil.tutor.entity.user.Student;
import com.kil.tutor.entity.user.Tutor;
import com.kil.tutor.entity.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;
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

    public ChatInfo map(Chat chat, Long requestedUserId, Long unreadMessageCount, MessageInfo lastMessage) {
        if (chat instanceof DirectChat) {
            return this.map((DirectChat) chat, requestedUserId, unreadMessageCount, lastMessage);
        } else if (chat instanceof GroupChat) {
            return this.map((GroupChat) chat, unreadMessageCount, lastMessage);
        } else throw new UnsupportedOperationException();
    }

    @Mapping(target = "id", source = "direct.id")
    protected abstract DirectInfo map(DirectChat direct, Long requestedUserId, Long unreadMessageCount, MessageInfo lastMessage);

//    @AfterMapping
//    protected void afterDirectMap(
//            @MappingTarget DirectInfo directInfo,
//            DirectChat direct,
//            Long requestedUserId,
//            Long unreadMessageCount,
//            MessageInfo lastMessage
//    ) {
//        directInfo.set
//    }

    @Mapping(target = "id", source = "group.id")
    protected abstract GroupInfo map(GroupChat group, Long unreadMessageCount, MessageInfo lastMessage);

    protected Long mapToUserId(User user) {
        return user.getId();
    }

    public GetMessagesResponse map(Page<ChatMessage> messagePage) {
        GetMessagesResponse response = new GetMessagesResponse();
        response.setMessages(map(messagePage.toList()));
        response.setTotalElements(messagePage.getTotalElements());
        response.setTotalPages(messagePage.getTotalPages());
        return response;
    }

    public abstract List<MessageInfo> map(List<ChatMessage> messages);

    protected MessageInfo map(ChatMessage message) {
        if (message instanceof SimpleMessage) {
            return this.map((SimpleMessage) message);
        } else if (message instanceof Vote) {
            return this.map((Vote) message);
        } else throw new UnsupportedOperationException();
    }

    @Mapping(target = "text", source = "messageText")
    @Mapping(target = "userId", source = "author.id")
    protected abstract SimpleMessageInfo map(SimpleMessage message);

    @Mapping(target = "chatId", ignore = true)
    @Mapping(target = "text", source = "messageText")
    @Mapping(target = "userId", source = "author.id")
    protected abstract VoteInfo map(Vote vote);

    @Mapping(target = "text", source = "vote.messageText")
    @Mapping(target = "userId", source = "vote.author.id")
    protected abstract VoteInfo map(Vote vote, Long chatId);

    @Mapping(target = "votedUserIds", source = "votedUsers")
    protected abstract VoteOptionInfo map(VoteOption option);

    public abstract FindMessagesRequest map(Long chatId, GetMessagesRequest request);

    public abstract List<ReactionInfo> mapReactions(List<MessageReaction> reactions);


    @Mapping(target = "name", source = "reaction.name")
    @Mapping(target = "authorIds", source = "authors")
    public abstract ReactionInfo map(MessageReaction reaction);

    public abstract MessageReactionUpdate map(WebSocketReaction reaction);

    public abstract MessageReactionsInfo mapToMessageReactionInfo(Long messageId, Long chatId, List<MessageReaction> reactions);

    public abstract VoteRequest map(CreateVoteRequest request);

    public abstract Voting map(VotingRequest request);
}
