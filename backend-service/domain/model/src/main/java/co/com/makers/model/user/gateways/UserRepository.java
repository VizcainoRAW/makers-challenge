package co.com.makers.model.user.gateways;

import co.com.makers.model.user.User;
import co.com.makers.model.user.valueobject.LoginIdentifier;
import reactor.core.publisher.Mono;

public interface UserRepository {
    Mono<User> save(User user);

    Mono<Boolean> existsByLoginIdentifier(LoginIdentifier loginIdentifier);

    Mono<User> findByLoginIdentifier(LoginIdentifier loginIdentifier);
}
