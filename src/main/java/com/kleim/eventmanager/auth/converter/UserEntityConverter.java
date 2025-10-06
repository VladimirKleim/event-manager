package com.kleim.eventmanager.auth.converter;

import com.kleim.eventmanager.auth.pojo.User;
import com.kleim.eventmanager.auth.domain.UserRole;
import com.kleim.eventmanager.auth.pojo.UserEntity;
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
