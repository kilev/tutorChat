package com.kil.tutor;

import com.kil.tutor.domain.Role;
import com.kil.tutor.entity.user.Student;
import com.kil.tutor.entity.user.Tutor;
import com.kil.tutor.repository.StudentRepository;
import com.kil.tutor.repository.TutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.Collections;

@Service
public class InitSampleDataService {
    private final StudentRepository studentRepository;
    private final TutorRepository tutorRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public InitSampleDataService(
            StudentRepository studentRepository,
            TutorRepository tutorRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.studentRepository = studentRepository;
        this.tutorRepository = tutorRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    private void init() {
        Tutor tutor = new Tutor();
        tutor.setUsername("tutor");
        tutor.setPassword(passwordEncoder.encode("tutor"));
        tutor.setFirstName("tutorFirstName");
        tutor.setMiddleName("tutorMiddleName");
        tutor.setLastName("tutorLastName");
        tutor.setLastOnlineDate(LocalDateTime.now());
        tutor.setRoles(Collections.singletonList(Role.USER));
        tutorRepository.save(tutor);

        Student student = new Student();
        student.setUsername("student");
        student.setPassword(passwordEncoder.encode("student"));
        student.setFirstName("studentFirstName");
        student.setMiddleName("studentMiddleName");
        student.setLastName("studentLastName");
        student.setGroupName("AVT-713");
        student.setLastOnlineDate(LocalDateTime.now());
        student.setRoles(Collections.singletonList(Role.USER));
        studentRepository.save(student);
    }
}
