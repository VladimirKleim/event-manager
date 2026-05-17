package com.kleim.eventmanager.auth.pojo;

import com.kleim.eventmanager.auth.domain.UserRole;

public record UserDTO(

        Long id,
        String login,
        Integer age,
        String email,
        UserRole role

) {}
