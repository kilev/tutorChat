package com.kil.tutor.controller;

import com.kil.tutor.dto.chat.GetUsersRequest;
import com.kil.tutor.dto.user.GetUsersResponse;
import com.kil.tutor.entity.user.User;
import com.kil.tutor.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping(ApiConsts.USER)
@RestController
public class UserController {
    private final ServiceMapper mapper;
    private final UserService userService;

    @Autowired
    public UserController(
            ServiceMapper mapper,
            UserService userService
    ) {
        this.mapper = mapper;
        this.userService = userService;
    }

    @PostMapping(ApiConsts.ALL)
    public GetUsersResponse getUsers(@RequestBody GetUsersRequest request) {
        List<User> users = userService.getUsers(request.getUserIds());
        return GetUsersResponse.builder().users(mapper.mapUsers(users)).build();
    }
}
