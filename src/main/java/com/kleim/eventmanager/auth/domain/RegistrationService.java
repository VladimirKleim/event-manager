package com.kleim.eventmanager.auth.domain;

import com.kleim.eventmanager.auth.pojo.SignUpRequest;
import com.kleim.eventmanager.auth.pojo.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegistrationService {

    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    public RegistrationService(PasswordEncoder passwordEncoder, UserService userService) {
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
    }

    public User saveUser(
            SignUpRequest signUpRequest
    ) {
        if(userService.isUserExistsByLogin(signUpRequest.login())) {
            throw new IllegalArgumentException("User login already exists");
        }
        if (userService.isUserExistsByEmail(signUpRequest.email())) {
            throw new IllegalArgumentException("This email already exists");
        }
        var encodePassword = passwordEncoder.encode(signUpRequest.password());
        var user = new User(
                null,
                signUpRequest.login(),
                encodePassword,
                signUpRequest.email(),
                signUpRequest.age(),
                UserRole.USER
        );

        return userService.createUser(user);
    }
}
