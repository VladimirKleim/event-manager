package com.kleim.eventmanager.controller;

import com.kleim.eventmanager.auth.UserDTO;
import com.kleim.eventmanager.auth.UserSingInRequest;
import com.kleim.eventmanager.converter.UserDTOconverter;
import com.kleim.eventmanager.service.UserRegisterService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    private final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserRegisterService userRegisterService;
    private final UserDTOconverter userDTOconverter;

    public UserController(UserRegisterService userRegisterService, UserDTOconverter userDTOconverter) {
        this.userRegisterService = userRegisterService;
        this.userDTOconverter = userDTOconverter;
    }

    @PostMapping
    public ResponseEntity<UserDTO> signInUser(
          @RequestBody @Valid UserSingInRequest userSingInRequest
    ) {
        var savedUser = userRegisterService.saveUser(userSingInRequest);
       return ResponseEntity.status(HttpStatus.CREATED).body(userDTOconverter.toDtoUser(savedUser));
    }


    @PostMapping("/auth")
    public ResponseEntity<UserDTO> signUpUser(

    ) {
        return null;
    }
}
