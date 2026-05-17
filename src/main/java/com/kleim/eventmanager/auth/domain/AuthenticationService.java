package com.kleim.eventmanager.auth.domain;

import com.kleim.eventmanager.auth.pojo.AuthenticatedUser;
import com.kleim.eventmanager.auth.pojo.SignInRequest;
import com.kleim.eventmanager.security.token.JwtTokenManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
       var user = userService.getUserByLogin(signInRequest.login());

       if (!passwordEncoder.matches(signInRequest.password(), user.password())) {
           throw new BadCredentialsException("Not valid login or password");
        }

       return jwtTokenManager.generateToken(user);
    }

    public AuthenticatedUser getCurrentAuthUser() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof AuthenticatedUser authenticatedUser)) {
            throw new UsernameNotFoundException("Auth not present");
        }
        return authenticatedUser;
    }
}
