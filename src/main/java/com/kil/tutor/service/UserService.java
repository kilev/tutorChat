package com.kil.tutor.service;

import com.kil.tutor.entity.user.User;
import com.kil.tutor.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getUsers(List<Long> userIds) {
        return userRepository.findAllById(userIds);
    }

    public byte[] getAvatar(Long userId) {
        User user = userRepository.getOne(userId);
        return user.getAvatar();
    }

}
