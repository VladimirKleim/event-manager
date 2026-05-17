package com.kleim.eventmanager.auth;

import com.kleim.eventmanager.auth.domain.UserRole;
import com.kleim.eventmanager.auth.domain.UserService;
import com.kleim.eventmanager.auth.pojo.User;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ApplicationContextEvent;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
public class InitializrDefaultEntity {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public InitializrDefaultEntity(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void starterInitialEntity() {
        createUser("admin", "admin", "test1@gmail.com", UserRole.ADMIN);
        createUser("user", "user", "test2@gmail.com", UserRole.USER);
    }

    private void createUser(
            String login,
            String password,
            String email,
            UserRole role
    ) {
        if (userService.isUserExistsByLogin(login)) {
            return;
        }
        var encodePassword = passwordEncoder.encode(password);
        var user  = new User(
                null,
                login,
                encodePassword,
                email,
                30,
                role
        );

        userService.createUser(user);
    }
}
