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
        tutor.setFirstName("Степан");
        tutor.setMiddleName("Георгиевич");
        tutor.setLastName("Тарасов");
        tutor.setRoles(Collections.singletonList(Role.USER));
        tutor.setStatus("Готов принимать долги!");

        Tutor tutor1 = new Tutor();
        tutor1.setUsername("tutor_tretyakov");
        tutor1.setPassword(passwordEncoder.encode("tutor_tretyakov"));
        tutor1.setFirstName("Роман");
        tutor1.setMiddleName("Кириллович");
        tutor1.setLastName("Третьяков");
        tutor1.setRoles(Collections.singletonList(Role.USER));
        tutor1.setStatus("На совещании.");

        Student student = new Student();
        student.setUsername("student_starostina");
        student.setPassword(passwordEncoder.encode("student_starostina"));
        student.setFirstName("Ника");
        student.setMiddleName("Ивановна");
        student.setLastName("Старостина");
        student.setGroupName("AVT-713");
        student.setRoles(Collections.singletonList(Role.USER));

        Student studentPetya = new Student();
        studentPetya.setUsername("petya");
        studentPetya.setPassword(passwordEncoder.encode("petya"));
        studentPetya.setFirstName("Петр");
        studentPetya.setMiddleName("Алексеевич");
        studentPetya.setLastName("Сакимов");
        studentPetya.setGroupName("AVT-713");

        Student student1 = new Student();
        student1.setUsername("student");
        student1.setPassword(passwordEncoder.encode("student"));
        student1.setFirstName("Максим");
        student1.setMiddleName("Ярославович");
        student1.setLastName("Соколов");
        student1.setGroupName("AVT-713");
        student1.setRoles(Collections.singletonList(Role.USER));

        Student student2 = new Student();
        student2.setUsername("student_smirnov");
        student2.setPassword(passwordEncoder.encode("student_smirnov"));
        student2.setFirstName("Даниил");
        student2.setMiddleName("Иванович");
        student2.setLastName("Смирнов");
        student2.setGroupName("AVT-713");
        student2.setRoles(Collections.singletonList(Role.USER));

        Student student3 = new Student();
        student3.setUsername("student_salnikova");
        student3.setPassword(passwordEncoder.encode("student_salnikova"));
        student3.setFirstName("Мия");
        student3.setMiddleName("Данииловна");
        student3.setLastName("Сальникова");
        student3.setGroupName("AVT-712");
        student3.setRoles(Collections.singletonList(Role.USER));

        Student student4 = new Student();
        student4.setUsername("student_simonova");
        student4.setPassword(passwordEncoder.encode("student_simonova"));
        student4.setFirstName("Карина");
        student4.setMiddleName("Олеговна");
        student4.setLastName("Симонова");
        student4.setGroupName("AVT-712");
        student4.setRoles(Collections.singletonList(Role.USER));

        Student student5 = new Student();
        student5.setUsername("student_solovyeva");
        student5.setPassword(passwordEncoder.encode("student_solovyeva"));
        student5.setFirstName("София");
        student5.setMiddleName("Александровна");
        student5.setLastName("Соловьева");
        student5.setGroupName("AVT-712");
        student5.setRoles(Collections.singletonList(Role.USER));

        studentPetya.setRoles(Collections.singletonList(Role.USER));
        userRepository.saveAll(Arrays.asList(tutor, tutor1, student, student1, student2, student3, student4, student5, studentPetya));

        DirectChat direct1 = new DirectChat();
        direct1.setParticipants(Arrays.asList(tutor, student));

        DirectChat direct2 = new DirectChat();
        direct2.setParticipants(Arrays.asList(tutor, studentPetya));

        DirectChat direct = new DirectChat();
        direct.setParticipants(Arrays.asList(tutor, tutor1));

        GroupChat groupAVT713 = new GroupChat();
        groupAVT713.setParticipants(Arrays.asList(tutor, student, studentPetya, student1, student2));
        groupAVT713.setName("AVT-713");

        GroupChat groupAvt712 = new GroupChat();
        groupAvt712.setParticipants(Arrays.asList(tutor, student3, student4, student5));
        groupAvt712.setName("AVT-712");
        chatRepository.saveAll(Arrays.asList(direct1, direct2, direct, groupAVT713, groupAvt712));

        ChatMessage initDirectMessage = new SimpleMessage();
        initDirectMessage.setChat(direct1);
        initDirectMessage.setAuthor(tutor);
        initDirectMessage.setMessageText("Здравствуй, Ника. Не забудьте сдать курсовую до следующего понедельника.");

        ChatMessage initGroupMessage = new SimpleMessage();
        initGroupMessage.setChat(groupAVT713);
        initGroupMessage.setAuthor(tutor);
        initGroupMessage.setMessageText("Здрвствуйте студенты! С началом учебного года вас!");

        VoteOption firstOption = new VoteOption("Завтра");
        firstOption.setVotedUsers(Arrays.asList(student1, student2));
        VoteOption secondOption = new VoteOption("Через неделю");
        VoteOption thirdOption = new VoteOption("Через месяц");
        List<VoteOption> initGroupVoteOptions = Arrays.asList(firstOption, secondOption, thirdOption);
        optionRepository.saveAll(initGroupVoteOptions);

        Vote initGroupVote = new Vote();
        initGroupVote.setChat(groupAVT713);
        initGroupVote.setAuthor(tutor);
        initGroupVote.setMessageText("Когда будем сдавать долги?");
        initGroupVote.setOptions(initGroupVoteOptions);

        ChatMessage answerGroupMessage = new SimpleMessage();
        answerGroupMessage.setChat(groupAVT713);
        answerGroupMessage.setAuthor(tutor);
        answerGroupMessage.setMessageText("Степан Георгиевич, а отчеты по ботанике обязательно делать?");

        messageRepository.saveAll(Arrays.asList(initDirectMessage, initGroupMessage, initGroupVote, answerGroupMessage));

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
        initGroupMessageReaction.setAuthors(Arrays.asList(student, studentPetya));
        messageReactionRepository.saveAll(Arrays.asList(initDirectMessageReaction, initGroupMessageReaction));
    }

    @SneakyThrows
    private byte[] loadImage(String ImagePath) {
        InputStream inputStream = this.getClass().getResourceAsStream(ImagePath);
        return IOUtils.toByteArray(inputStream);
    }
}
