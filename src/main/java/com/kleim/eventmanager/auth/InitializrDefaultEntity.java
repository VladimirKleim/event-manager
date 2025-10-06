package com.kleim.eventmanager.auth;

import com.kleim.eventmanager.auth.domain.UserRole;
import com.kleim.eventmanager.auth.domain.UserService;
import com.kleim.eventmanager.auth.pojo.User;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class InitializrDefaultEntity {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public InitializrDefaultEntity(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @EventListener(ContextStartedEvent.class)
    public void starterInitialEntity() {
        createUser("admin", "admin", UserRole.ADMIN);
        createUser("user", "user", UserRole.USER);
    }

    private void createUser(
            String login,
            String password,
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
                30,
                role
        );
        userService.createUser(user);

    }
}
