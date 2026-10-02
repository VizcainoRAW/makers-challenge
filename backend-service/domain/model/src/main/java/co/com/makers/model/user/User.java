package co.com.makers.model.user;
import co.com.makers.model.user.valueobject.LoginIdentifier;import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
//import lombok.NoArgsConstructor;
import lombok.Setter;import java.util.UUID;


public record User (
        UUID id,
        LoginIdentifier loginIdentifier,
        String password
){

    public User register(LoginIdentifier loginIdentifier, String password) {
        return new User(UUID.randomUUID(), loginIdentifier, password);
    }
}
