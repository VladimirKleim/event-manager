package com.kleim.eventmanager.auth.api;

import com.kleim.eventmanager.auth.domain.AuthenticationService;
import com.kleim.eventmanager.mapper.UserMapper;
import com.kleim.eventmanager.auth.domain.RegistrationService;
import com.kleim.eventmanager.auth.domain.UserService;
import com.kleim.eventmanager.auth.pojo.SignInRequest;
import com.kleim.eventmanager.auth.pojo.SignUpRequest;
import com.kleim.eventmanager.auth.pojo.UserDTO;
import com.kleim.eventmanager.security.token.JwtTokenResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/users")
public class UserController {

    private final Logger log = LoggerFactory.getLogger(UserController.class);
    private final RegistrationService registrationService;
    private final UserMapper userDTOconverter;
    private final UserService userService;
    private final AuthenticationService authenticationService;

    public UserController(RegistrationService registrationService, UserMapper userDTOconverter, UserService userService, AuthenticationService authenticationService) {
        this.registrationService = registrationService;
        this.userDTOconverter = userDTOconverter;
        this.userService = userService;
        this.authenticationService = authenticationService;
    }

    @PostMapping
    public ResponseEntity<UserDTO> signUpUser(
          @RequestBody @Valid SignUpRequest signUpRequest
    ) {
        log.info("User register with: login={}, age={}", signUpRequest.login(), signUpRequest.age());
        var savedUser = registrationService.saveUser(signUpRequest);
       return ResponseEntity.status(HttpStatus.CREATED).body(userDTOconverter.toDtoUser(savedUser));
    }

    @PostMapping("/auth")
    public ResponseEntity<JwtTokenResponse> signInUser(
          @RequestBody @Valid SignInRequest signInRequest
    ) {
        log.info("User success sing-in with login={}", signInRequest.login());
        var token = authenticationService.authUser(signInRequest);
        return ResponseEntity.ok(new JwtTokenResponse(token));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(
            @PathVariable("id") Long id
    ) {
        log.info("Got register user with id={}", id);
        var gotUser = userService.getUserById(id);
        return ResponseEntity.ok(userDTOconverter.toDtoUser(gotUser));
    }
}
