package co.com.makers.api.dto.user;

public record LoginResponse(
        String accessToken,
        String tokenType,
        Long expiresIn
) {}
