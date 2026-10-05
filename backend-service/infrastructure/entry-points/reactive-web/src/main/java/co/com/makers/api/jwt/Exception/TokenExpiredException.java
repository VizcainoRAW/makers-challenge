package co.com.makers.api.jwt.Exception;

public class TokenExpiredException extends JwtValidationException {

    public TokenExpiredException(String message) {
        super(message);
    }
}