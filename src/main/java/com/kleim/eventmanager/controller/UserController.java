package com.kleim.eventmanager.controller;

import com.kleim.eventmanager.auth.SignInRequest;
import com.kleim.eventmanager.auth.UserDTO;
import com.kleim.eventmanager.converter.UserDTOconverter;
import com.kleim.eventmanager.repository.UserRepository;
import com.kleim.eventmanager.security.token.JwtTokenManager;
import com.kleim.eventmanager.security.token.JwtTokenResponse;
import com.kleim.eventmanager.service.AuthenticationService;
import com.kleim.eventmanager.service.UserRegisterService;
import com.kleim.eventmanager.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserRegisterService userRegisterService;
    private final UserDTOconverter userDTOconverter;
    private final UserRepository userRepository;
    private final UserService userService;

    private final AuthenticationService authenticationService;

    private final JwtTokenManager jwtTokenManager;

    public UserController(UserRegisterService userRegisterService, UserDTOconverter userDTOconverter, UserRepository userRepository, UserService userService, AuthenticationService authenticationService, JwtTokenManager jwtTokenManager) {
        this.userRegisterService = userRegisterService;
        this.userDTOconverter = userDTOconverter;
        this.userRepository = userRepository;
        this.userService = userService;
        this.authenticationService = authenticationService;
        this.jwtTokenManager = jwtTokenManager;
    }

    @PostMapping
    public ResponseEntity<UserDTO> signUpUser(
          @RequestBody @Valid SignUpRequest signUpRequest
    ) {
        var savedUser = userRegisterService.saveUser(signUpRequest);
        var token = jwtTokenManager.generateToken(savedUser);
        log.info(token);
        log.info(jwtTokenManager.isTokenValid(token) + "");
        log.info(jwtTokenManager.getLoginFromToken(token));
       return ResponseEntity.status(HttpStatus.CREATED).body(userDTOconverter.toDtoUser(savedUser));
    }


    @PostMapping("/auth")
    public ResponseEntity<JwtTokenResponse> signInUser(
          @RequestBody SignInRequest signInRequest
    ) {
        var token = authenticationService.authUser(signInRequest);
        return ResponseEntity.ok(new JwtTokenResponse(token));
    }
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(
            @PathVariable("id") Long id
    ) {
        var gotUser = userService.getUserById(id);
        return ResponseEntity.ok(userDTOconverter.toDtoUser(gotUser));
    }
}
