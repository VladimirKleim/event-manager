package com.kleim.eventmanager.service;

import com.kleim.eventmanager.auth.SignInRequest;
import com.kleim.eventmanager.security.token.JwtTokenManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final JwtTokenManager jwtTokenManager;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationService(JwtTokenManager jwtTokenManager, UserService userService, PasswordEncoder passwordEncoder) {
        this.jwtTokenManager = jwtTokenManager;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    public String authUser(SignInRequest signInRequest) {
        if (!userService.isUserExistsByLogin(signInRequest.login())) {
            throw new BadCredentialsException("Bad credentials");
        }
        var user = userService.getUserByLogin(signInRequest.login());
       if (!passwordEncoder.matches(signInRequest.password(), user.password())) {
           throw new BadCredentialsException("Not valid login");
        }
       return jwtTokenManager.generateToken(user);
    }
}
