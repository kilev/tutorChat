package com.kil.tutor;

import com.kil.tutor.domain.Role;
import com.kil.tutor.entity.chat.DirectChat;
import com.kil.tutor.entity.chat.GroupChat;
import com.kil.tutor.entity.user.Student;
import com.kil.tutor.entity.user.Tutor;
import com.kil.tutor.repository.ChatRepository;
import com.kil.tutor.repository.StudentRepository;
import com.kil.tutor.repository.TutorRepository;
import com.kil.tutor.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

@Service
public class InitSampleDataService {
    public final ChatRepository chatRepository;
    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final TutorRepository tutorRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public InitSampleDataService(
            ChatRepository chatRepository,
            UserRepository userRepository,
            StudentRepository studentRepository,
            TutorRepository tutorRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.chatRepository = chatRepository;
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.tutorRepository = tutorRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    private void init() {
        Tutor tutor = new Tutor();
        tutor.setUsername("tutor");
        tutor.setPassword(passwordEncoder.encode("tutor"));
        tutor.setFirstName("Томара");
        tutor.setMiddleName("Ивановна");
        tutor.setLastName("Иванова");
        tutor.setLastOnlineDate(LocalDateTime.now());
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
        student.setLastOnlineDate(LocalDateTime.now());
        student.setRoles(Collections.singletonList(Role.USER));
        userRepository.save(student);

        DirectChat direct = new DirectChat();
        direct.setParticipants(Arrays.asList(tutor, student));
        chatRepository.save(direct);

        GroupChat group = new GroupChat();
        group.setParticipants(Arrays.asList(tutor, student));
        group.setName("AVT-713");
        chatRepository.save(group);
    }
}
