package com.kleim.eventmanager.auth.domain;

import com.kleim.eventmanager.auth.converter.UserEntityConverter;
import com.kleim.eventmanager.auth.pojo.User;
import com.kleim.eventmanager.auth.pojo.UserRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserEntityConverter userEntityConverter;


    public UserService(UserRepository userRepository, UserEntityConverter userEntityConverter) {
        this.userRepository = userRepository;
        this.userEntityConverter = userEntityConverter;

    }

    public User createUser(User user) {
           var toEntity = userRepository.save(userEntityConverter.toEntityUser(user));
           return userEntityConverter.toDomain(toEntity);
       }

    public boolean isUserExistsByLogin(String login) {
        return userRepository.findByLogin(login).isPresent();
    }

    public User getUserByLogin(String login) {
        return userRepository.findByLogin(login)
                .map(userEntityConverter::toDomain)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .map(userEntityConverter::toDomain)
                .orElseThrow(() -> new EntityExistsException("User with id: %s not found".formatted(id)));
    }
}
