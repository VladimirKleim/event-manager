package com.kleim.eventmanager.auth.pojo;

import com.kleim.eventmanager.auth.domain.UserRole;

public record AuthenticatedUser(
        Long id,
        String login,
        UserRole role
) {

    public static AuthenticatedUser from(User user) {
        return new AuthenticatedUser(
                user.id(),
                user.login(),
                user.role()
        );
    }

}
