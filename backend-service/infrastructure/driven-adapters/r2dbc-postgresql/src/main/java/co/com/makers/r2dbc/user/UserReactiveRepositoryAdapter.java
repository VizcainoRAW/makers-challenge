package co.com.makers.r2dbc.user;

import co.com.makers.model.user.User;
import co.com.makers.model.user.gateways.UserRepository;
import co.com.makers.model.user.valueobject.LoginIdentifier;
import co.com.makers.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.UUID;

@Repository
public class UserReactiveRepositoryAdapter extends ReactiveAdapterOperations<
        User,
        UserEntity,
        UUID,
        UserReactiveRepository
> implements UserRepository {

    public UserReactiveRepositoryAdapter(UserReactiveRepository repository, ObjectMapper mapper) {
        super(repository, mapper, UserMapper::toDomain);
    }

    @Override
    protected UserEntity toData(User entity) {
        return UserMapper.toEntity(entity);
    }

    @Override
    public Mono<Boolean> existsByLoginIdentifier(LoginIdentifier loginIdentifier) {
        if (loginIdentifier == null || loginIdentifier.getValue() == null) {
            return Mono.just(false);
        }
        return repository.existsByLoginIdentifier(loginIdentifier.getValue());
    }

    @Override
    public Mono<User> findByLoginIdentifier(LoginIdentifier loginIdentifier) {
        if (loginIdentifier == null || loginIdentifier.getValue() == null) {
            return Mono.empty();
        }
        return repository.findByLoginIdentifier(loginIdentifier.getValue())
                .map(UserMapper::toDomain);
    }

    @Override
    public Flux<User> findAllByIds(Collection<UUID> ids) {
        if (ids == null || ids.isEmpty()) {
            return Flux.empty();
        }
        return repository.findAllById(ids)
                .map(UserMapper::toDomain);
    }
}
