package com.kleim.eventmanager.service;

import com.kleim.eventmanager.auth.User;
import com.kleim.eventmanager.auth.UserRole;
import com.kleim.eventmanager.auth.UserSingInRequest;
import com.kleim.eventmanager.entity.UserEntity;
import com.kleim.eventmanager.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserRegisterService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    public UserRegisterService(UserRepository userRepository, PasswordEncoder passwordEncoder, UserService userService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
    }

    public User saveUser(
            UserSingInRequest userSingInRequest
    ) {
        if(userService.isUserExistsByLogin(userSingInRequest.login())) {
            throw new IllegalArgumentException("...");
        }
        var encodePassword = passwordEncoder.encode(userSingInRequest.password());
        var user = new User(
                null,
                userSingInRequest.login(),
                encodePassword,
                userSingInRequest.age(),
                UserRole.USER
        );
        return userService.createUser(user);
    }
}
