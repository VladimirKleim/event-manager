package com.kleim.eventmanager.auth.domain;

import com.kleim.eventmanager.mapper.UserDbMapper;
import com.kleim.eventmanager.auth.pojo.User;
import com.kleim.eventmanager.auth.pojo.UserRepository;
import jakarta.persistence.EntityExistsException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserDbMapper userDbMapper;

    public UserService(UserRepository userRepository, UserDbMapper userDbMapper) {
        this.userRepository = userRepository;
        this.userDbMapper = userDbMapper;

    }

    public User createUser(User user) {
           var toEntity = userRepository.save(userDbMapper.toEntityUser(user));
           return userDbMapper.toDomain(toEntity);
    }

    public boolean isUserExistsByLogin(String login) {
        return userRepository.findByLogin(login).isPresent();
    }

    public User getUserByLogin(String login) {
        return userRepository.findByLogin(login)
                .map(userDbMapper::toDomain)
                .orElseThrow(() -> new BadCredentialsException("Not valid login or password"));
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .map(userDbMapper::toDomain)
                .orElseThrow(() -> new EntityExistsException("User with id: %s not found".formatted(id)));
    }

    public boolean isUserExistsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
