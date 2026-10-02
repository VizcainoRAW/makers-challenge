package co.com.makers.api.dto.user;

import co.com.makers.model.user.User;

import java.util.UUID;

public record UserResponse(
        UUID id,
        String role,
        String loginIdentifier,
        Boolean active
) {
    public static UserResponse fromDomain(User user) {
        if (user == null) return null;
        return new UserResponse(
                user.id(),
                user.role() != null ? user.role().name() : null,
                user.loginIdentifier() != null ? user.loginIdentifier().getValue() : null,
                user.active()
        );
    }
}
