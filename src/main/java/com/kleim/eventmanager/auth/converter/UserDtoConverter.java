package com.kleim.eventmanager.auth.converter;

import com.kleim.eventmanager.auth.pojo.User;
import com.kleim.eventmanager.auth.pojo.UserDTO;
import org.springframework.stereotype.Component;

@Component
public class UserDtoConverter {

     public UserDTO toDtoUser(User user) {
         return new UserDTO(
                 user.id(),
                 user.login(),
                 user.age(),
                 user.role()
         );
     }

}
