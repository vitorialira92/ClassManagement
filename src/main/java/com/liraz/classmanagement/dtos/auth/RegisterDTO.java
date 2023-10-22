package com.liraz.classmanagement.dtos.auth;

import com.liraz.classmanagement.domain.user.UserRole;
import jakarta.validation.constraints.NotNull;

public record RegisterDTO(@NotNull String login, @NotNull String password, @NotNull UserRole userRole) {
}
