package co.com.makers.r2dbc.user;

import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface UserReactiveRepository extends ReactiveCrudRepository<UserEntity, UUID>,
        ReactiveQueryByExampleExecutor<UserEntity> {
    Mono<Boolean> existsByLoginIdentifier(String loginIdentifier);

    Mono<UserEntity> findByLoginIdentifier(String loginIdentifier);
}
