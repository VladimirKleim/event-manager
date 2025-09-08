package com.kleim.eventmanager.service;

import com.kleim.eventmanager.auth.User;
import com.kleim.eventmanager.auth.UserRole;
import com.kleim.eventmanager.controller.SignUpRequest;
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
            SignUpRequest signUpRequest
    ) {
        if(userService.isUserExistsByLogin(signUpRequest.login())) {
            throw new IllegalArgumentException("User login already exists");
        }
        var encodePassword = passwordEncoder.encode(signUpRequest.password());
        var user = new User(
                null,
                signUpRequest.login(),
                encodePassword,
                signUpRequest.age(),
                UserRole.USER
        );
        return userService.createUser(user);
    }
}
