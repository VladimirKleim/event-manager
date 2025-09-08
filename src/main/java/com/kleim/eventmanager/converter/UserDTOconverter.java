package com.kleim.eventmanager.converter;

import com.kleim.eventmanager.auth.User;
import com.kleim.eventmanager.auth.UserDTO;
import org.springframework.stereotype.Component;

@Component
public class UserDTOconverter {

     public UserDTO toDtoUser(User user) {
         return new UserDTO(
                 user.id(),
                 user.login(),
                 user.age(),
                 user.role()
         );
     }

}
