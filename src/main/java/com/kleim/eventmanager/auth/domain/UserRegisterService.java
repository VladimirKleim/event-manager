package com.kleim.eventmanager.auth.domain;

import com.kleim.eventmanager.auth.pojo.SignUpRequest;
import com.kleim.eventmanager.auth.pojo.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserRegisterService {

    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    public UserRegisterService(PasswordEncoder passwordEncoder, UserService userService) {
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
