package co.com.makers.model.user.exceptions;

public class UserDomainException extends RuntimeException{

    protected UserDomainException(String message) {
        super(message);
    }
}
