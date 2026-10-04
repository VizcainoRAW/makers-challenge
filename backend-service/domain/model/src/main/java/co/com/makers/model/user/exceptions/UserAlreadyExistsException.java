package co.com.makers.model.user.exceptions;

import co.com.makers.model.user.valueobject.LoginIdentifier;

public class UserAlreadyExistsException extends UserDomainException {

    public UserAlreadyExistsException(LoginIdentifier loginIdentifier) {
        super("the user " + loginIdentifier + " already exists");
    }
}
