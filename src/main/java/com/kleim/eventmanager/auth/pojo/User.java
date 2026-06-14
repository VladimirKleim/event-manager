package com.kleim.eventmanager.auth.pojo;

import com.kleim.eventmanager.auth.domain.UserRole;

public record User(

        Long id,
        String login,
        String password,
        String email,
        Integer age,
        UserRole role

) {}
