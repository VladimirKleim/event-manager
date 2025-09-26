package com.kleim.eventmanager.auth.pojo;

import jakarta.validation.constraints.NotBlank;

public record SignInRequest(

        @NotBlank
        String login,
        @NotBlank
        String password
) {
}
