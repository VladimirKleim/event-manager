package com.kleim.eventmanager.controller;

import com.kleim.eventmanager.auth.UserSingIn;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class UserController {

    private final Logger log = LoggerFactory.getLogger(UserController.class);

    public ResponseEntity<Void> signInUser(
          @RequestBody @Valid UserSingIn userSingIn
    ) {

    }
}
