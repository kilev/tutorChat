package com.kil.tutor;

import com.kil.tutor.domain.Role;
import com.kil.tutor.entity.chat.DirectChat;
import com.kil.tutor.entity.chat.GroupChat;
import com.kil.tutor.entity.chat.message.ChatMessage;
import com.kil.tutor.entity.chat.message.MessageReaction;
import com.kil.tutor.entity.chat.message.Reaction;
import com.kil.tutor.entity.chat.message.SimpleMessage;
import com.kil.tutor.entity.user.Student;
import com.kil.tutor.entity.user.Tutor;
import com.kil.tutor.repository.*;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.Collections;

@Slf4j
@Service
public class InitSampleDataService {
    private static final String PNG_FORMAT = "png";

    private final ChatRepository chatRepository;
    private final MessageRepository messageRepository;
    private final ReactionRepository reactionRepository;
    private final MessageReactionRepository messageReactionRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public InitSampleDataService(
            ChatRepository chatRepository,
            MessageRepository messageRepository,
            ReactionRepository reactionRepository,
            MessageReactionRepository messageReactionRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.chatRepository = chatRepository;
        this.messageRepository = messageRepository;
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
        tutor.setFirstName("Томара");
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
        initDirectMessage.setMessageText("Привет, студент!");

        ChatMessage initGroupMessage = new SimpleMessage();
        initGroupMessage.setChat(group);
        initGroupMessage.setAuthor(tutor);
        initGroupMessage.setMessageText("Привет, студенты!");
        messageRepository.saveAll(Arrays.asList(initDirectMessage, initGroupMessage));

        Reaction likeReaction = new Reaction();
        likeReaction.setName("LIKE");
        likeReaction.setIcon(loadImage("classpath:icons/like.png", PNG_FORMAT));
        Reaction dislikeReaction = new Reaction();
        dislikeReaction.setName("DISLIKE");
        dislikeReaction.setIcon(loadImage("classpath:icons/dislike.png", PNG_FORMAT));
        Reaction zombieLikeReaction = new Reaction();
        zombieLikeReaction.setName("ZOMBIE_LIKE");
        zombieLikeReaction.setIcon(loadImage("classpath:icons/zombie-like.png", PNG_FORMAT));
        reactionRepository.saveAll(Arrays.asList(likeReaction, dislikeReaction, zombieLikeReaction));

        MessageReaction initDirectMessageReaction = new MessageReaction();
        initDirectMessageReaction.setMessage(initDirectMessage);
        initDirectMessageReaction.setReaction(likeReaction);
        initDirectMessageReaction.setAuthors(Collections.singletonList(student));

        MessageReaction initGroupMessageReaction = new MessageReaction();
        initGroupMessageReaction.setMessage(initGroupMessage);
        initGroupMessageReaction.setReaction(dislikeReaction);
        initGroupMessageReaction.setAuthors(Collections.singletonList(tutor));
        messageReactionRepository.saveAll(Arrays.asList(initDirectMessageReaction, initGroupMessageReaction));
    }

    @SneakyThrows
    private byte[] loadImage(String imageName, String format) {
        BufferedImage bImage = ImageIO.read(ResourceUtils.getFile(imageName));
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageIO.write(bImage, format, bos);
        return bos.toByteArray();
    }
}
