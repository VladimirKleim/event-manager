package com.kleim.eventmanager.service;

import com.kleim.eventmanager.auth.User;
import com.kleim.eventmanager.auth.UserRole;
import com.kleim.eventmanager.auth.UserSingInRequest;
import com.kleim.eventmanager.converter.UserDTOconverter;
import com.kleim.eventmanager.converter.UserEntityConverter;
import com.kleim.eventmanager.entity.UserEntity;
import com.kleim.eventmanager.repository.UserRepository;
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
        return userRepository.existsByLogin(login);
    }
}
