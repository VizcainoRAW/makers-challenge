package co.com.makers.model.user;
import co.com.makers.model.user.valueobject.LoginIdentifier;
import co.com.makers.model.user.valueobject.Role;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
//import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;


public record User (
        UUID id,
        Role role,
        LoginIdentifier loginIdentifier,
        String password,
        Boolean active
){

    public User register(Role role, LoginIdentifier loginIdentifier, String password) {
        return new User(UUID.randomUUID(), role, loginIdentifier, password, true);
    }
}
