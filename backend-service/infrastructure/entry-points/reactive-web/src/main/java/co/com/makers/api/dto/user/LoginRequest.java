package co.com.makers.api.dto.user;

public record LoginRequest(
        String loginIdentifier,
        String password
) {}
