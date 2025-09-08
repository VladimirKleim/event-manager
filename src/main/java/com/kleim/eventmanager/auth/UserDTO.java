package com.kleim.eventmanager.auth;

public record UserDTO(
        Long id,
        String login,
        Integer age,
        UserRole role
) {
}
