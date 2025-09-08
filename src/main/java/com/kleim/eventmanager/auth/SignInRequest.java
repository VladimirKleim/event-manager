package com.kleim.eventmanager.auth;

import jakarta.validation.constraints.NotBlank;

public record SignInRequest(
        @NotBlank
          String login,
          @NotBlank
          String password
) {
}
