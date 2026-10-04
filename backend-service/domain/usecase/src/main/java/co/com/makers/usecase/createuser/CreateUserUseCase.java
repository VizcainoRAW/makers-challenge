package co.com.makers.usecase.createuser;

import co.com.makers.model.user.User;
import co.com.makers.model.user.exceptions.UserAlreadyExistsException;
import co.com.makers.model.user.gateways.UserRepository;
import co.com.makers.model.user.valueobject.LoginIdentifier;
import co.com.makers.model.user.valueobject.Role;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class CreateUserUseCase {

    private final UserRepository userRepository;

    public Mono <User> createUser(Role role, LoginIdentifier loginIdentifier, String rawPassword) {
        return userRepository.existsByLoginIdentifier(loginIdentifier)
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.error(new UserAlreadyExistsException(loginIdentifier));
                    }
                    User newUser = User.register(role, loginIdentifier, rawPassword);
                    return userRepository.save(newUser);
                });
    }
}
