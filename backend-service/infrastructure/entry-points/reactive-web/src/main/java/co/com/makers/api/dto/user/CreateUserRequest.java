package co.com.makers.api.dto.user;

import co.com.makers.model.user.valueobject.Role;

public record CreateUserRequest(
        Role role,
        String loginIdentifier,
        String password
) {}