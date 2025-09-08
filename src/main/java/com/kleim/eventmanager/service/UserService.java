package com.kleim.eventmanager.service;

import com.kleim.eventmanager.auth.User;
import com.kleim.eventmanager.converter.UserEntityConverter;
import com.kleim.eventmanager.repository.UserRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.NotBlank;
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
