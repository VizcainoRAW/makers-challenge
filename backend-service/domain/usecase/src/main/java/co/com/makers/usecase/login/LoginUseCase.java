package co.com.makers.usecase.login;

import co.com.makers.model.user.User;
import co.com.makers.model.user.exceptions.UserBadCredentials;
import co.com.makers.model.user.gateways.UserRepository;
import co.com.makers.model.user.valueobject.LoginIdentifier;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class LoginUseCase {

    private final UserRepository userRepository;

    public Mono<User> login(LoginIdentifier loginIdentifier, String password) {
        if (loginIdentifier == null || loginIdentifier.getValue() == null || password == null) {
            return Mono.error(new IllegalArgumentException("Invalid login credentials"));
        }
        return userRepository.findByLoginIdentifier(loginIdentifier)
                .switchIfEmpty(Mono.error(new UserBadCredentials("Invalid credentials")))
                .flatMap(user -> {
                    if (user.active() != null && !user.active()) {
                        return Mono.error(new UserBadCredentials("User is not active"));
                    }
                    if (user.password() == null || !user.password().equals(password)) {
                        return Mono.error(new UserBadCredentials("Invalid credentials"));
                    }
                    return Mono.just(user);
                });
    }
}
