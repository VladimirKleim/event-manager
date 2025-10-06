package com.kleim.eventmanager.auth;

import com.kleim.eventmanager.service.UserService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.ApplicationContextEvent;
import org.springframework.context.event.ContextRefreshedEvent;
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

    @EventListener(ApplicationReadyEvent.class)
    public void starterInitialEntity() {
        createUser("admin", "admin", UserRole.ADMIN);
        createUser("use", "use", UserRole.USER);
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
