package com.kil.tutor;

import com.kil.tutor.domain.Role;
import com.kil.tutor.entity.chat.DirectChat;
import com.kil.tutor.entity.chat.GroupChat;
import com.kil.tutor.entity.chat.message.ChatMessage;
import com.kil.tutor.entity.chat.message.SimpleMessage;
import com.kil.tutor.entity.user.Student;
import com.kil.tutor.entity.user.Tutor;
import com.kil.tutor.repository.ChatRepository;
import com.kil.tutor.repository.MessageRepository;
import com.kil.tutor.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Collections;

@Service
public class InitSampleDataService {
    private final ChatRepository chatRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public InitSampleDataService(
            ChatRepository chatRepository,
            MessageRepository messageRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.chatRepository = chatRepository;
        this.messageRepository = messageRepository;
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
        userRepository.save(tutor);

        Student student = new Student();
        student.setUsername("student");
        student.setPassword(passwordEncoder.encode("student"));
        student.setFirstName("Иван");
        student.setMiddleName("Иванович");
        student.setLastName("Иванов");
        student.setGroupName("AVT-713");
        student.setRoles(Collections.singletonList(Role.USER));
        userRepository.save(student);

        DirectChat direct = new DirectChat();
        direct.setParticipants(Arrays.asList(tutor, student));
        chatRepository.save(direct);

        GroupChat group = new GroupChat();
        group.setParticipants(Arrays.asList(tutor, student));
        group.setName("AVT-713");
        chatRepository.save(group);

        ChatMessage initDirectMessage = new SimpleMessage();
        initDirectMessage.setChat(direct);
        initDirectMessage.setAuthor(tutor);
        initDirectMessage.setMessageText("Привет, студент!");
        messageRepository.save(initDirectMessage);

        ChatMessage initGroupMessage = new SimpleMessage();
        initGroupMessage.setChat(group);
        initGroupMessage.setAuthor(tutor);
        initGroupMessage.setMessageText("Привет, студенты!");
        messageRepository.save(initGroupMessage);
    }
}
