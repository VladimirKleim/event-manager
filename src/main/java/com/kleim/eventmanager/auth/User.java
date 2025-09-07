package com.kleim.eventmanager.auth;

public record User(
        Long id,
        String login,
        String password,
        UserRole role
) {
}
