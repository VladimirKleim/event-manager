package com.kleim.eventmanager.converter;

import com.kleim.eventmanager.auth.User;
import com.kleim.eventmanager.auth.UserRole;
import com.kleim.eventmanager.entity.UserEntity;
import com.kleim.eventmanager.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class UserEntityConverter {

    public User toDomain(UserEntity user) {
        return new User(
                user.getId(),
                user.getLogin(),
                user.getPassword(),
                user.getAge(),
                UserRole.valueOf(user.getRole())
        );
    }
    public UserEntity toEntityUser(User user) {
        return new UserEntity(
                user.id(),
                user.login(),
                user.password(),
                user.age(),
                user.role().name()
        );
    }
}
