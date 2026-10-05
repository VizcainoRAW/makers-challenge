package co.com.makers.model.user.exceptions;

public class UserBadCredentials extends UserDomainException {
    public UserBadCredentials(String message) {
        super(message);
    }
}
