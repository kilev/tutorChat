package com.kil.tutor;

import com.kil.tutor.domain.Role;
import com.kil.tutor.entity.chat.DirectChat;
import com.kil.tutor.entity.chat.GroupChat;
import com.kil.tutor.entity.chat.message.*;
import com.kil.tutor.entity.user.Student;
import com.kil.tutor.entity.user.Tutor;
import com.kil.tutor.repository.*;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class InitSampleDataService {
    private final ChatRepository chatRepository;
    private final MessageRepository messageRepository;
    private final VoteOptionRepository optionRepository;
    private final ReactionRepository reactionRepository;
    private final MessageReactionRepository messageReactionRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public InitSampleDataService(
            ChatRepository chatRepository,
            MessageRepository messageRepository,
            VoteOptionRepository optionRepository,
            ReactionRepository reactionRepository,
            MessageReactionRepository messageReactionRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.chatRepository = chatRepository;
        this.messageRepository = messageRepository;
        this.optionRepository = optionRepository;
        this.reactionRepository = reactionRepository;
        this.messageReactionRepository = messageReactionRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    @PostConstruct
    void init() {
        Tutor tutor = new Tutor();
        tutor.setUsername("tutor");
        tutor.setPassword(passwordEncoder.encode("tutor"));
        tutor.setFirstName("Тамара");
        tutor.setMiddleName("Ивановна");
        tutor.setLastName("Иванова");
        tutor.setRoles(Collections.singletonList(Role.USER));
        tutor.setStatus("Готова принимать долги!");

        Student student = new Student();
        student.setUsername("student");
        student.setPassword(passwordEncoder.encode("student"));
        student.setFirstName("Иван");
        student.setMiddleName("Иванович");
        student.setLastName("Иванов");
        student.setGroupName("AVT-713");
        student.setRoles(Collections.singletonList(Role.USER));
        userRepository.saveAll(Arrays.asList(tutor, student));

        DirectChat direct = new DirectChat();
        direct.setParticipants(Arrays.asList(tutor, student));

        GroupChat group = new GroupChat();
        group.setParticipants(Arrays.asList(tutor, student));
        group.setName("AVT-713");
        chatRepository.saveAll(Arrays.asList(direct, group));

        ChatMessage initDirectMessage = new SimpleMessage();
        initDirectMessage.setChat(direct);
        initDirectMessage.setAuthor(tutor);
        initDirectMessage.setMessageText("Здравствуй, Иван. Не забудьте сдать курсовую до следующего понедельника.");

        ChatMessage initGroupMessage = new SimpleMessage();
        initGroupMessage.setChat(group);
        initGroupMessage.setAuthor(tutor);
        initGroupMessage.setMessageText("Здрвствуйте студенты! С началом учебного года вас!");

        VoteOption firstOption = new VoteOption("Завтра");
        VoteOption secondOption = new VoteOption("Через неделю");
        VoteOption thirdOption = new VoteOption("Через месяц");
        List<VoteOption> initGroupVoteOptions = Arrays.asList(firstOption, secondOption, thirdOption);
        optionRepository.saveAll(initGroupVoteOptions);

        Vote initGroupVote = new Vote();
        initGroupVote.setChat(group);
        initGroupVote.setAuthor(tutor);
        initGroupVote.setMessageText("Когда будем сдавать долги?");
        initGroupVote.setOptions(initGroupVoteOptions);

        messageRepository.saveAll(Arrays.asList(initDirectMessage, initGroupMessage, initGroupVote));

        Reaction likeReaction = new Reaction();
        likeReaction.setName("LIKE");
        likeReaction.setIcon(loadImage("/icons/like.png"));
        Reaction dislikeReaction = new Reaction();
        dislikeReaction.setName("DISLIKE");
        dislikeReaction.setIcon(loadImage("/icons/dislike.png"));
        Reaction zombieLikeReaction = new Reaction();
        zombieLikeReaction.setName("ZOMBIE_LIKE");
        zombieLikeReaction.setIcon(loadImage("/icons/zombie-like.png"));
        reactionRepository.saveAll(Arrays.asList(likeReaction, dislikeReaction, zombieLikeReaction));

        MessageReaction initDirectMessageReaction = new MessageReaction();
        initDirectMessageReaction.setMessage(initDirectMessage);
        initDirectMessageReaction.setReaction(likeReaction);
        initDirectMessageReaction.setAuthors(Collections.singletonList(student));

        MessageReaction initGroupMessageReaction = new MessageReaction();
        initGroupMessageReaction.setMessage(initGroupMessage);
        initGroupMessageReaction.setReaction(dislikeReaction);
        initGroupMessageReaction.setAuthors(Collections.singletonList(student));
        messageReactionRepository.saveAll(Arrays.asList(initDirectMessageReaction, initGroupMessageReaction));
    }

    @SneakyThrows
    private byte[] loadImage(String ImagePath) {
        InputStream inputStream = this.getClass().getResourceAsStream(ImagePath);
        return IOUtils.toByteArray(inputStream);
    }
}
