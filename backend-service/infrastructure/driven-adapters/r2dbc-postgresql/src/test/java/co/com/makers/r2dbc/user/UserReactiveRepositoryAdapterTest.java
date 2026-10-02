package co.com.makers.r2dbc.user;

import co.com.makers.model.user.User;
import co.com.makers.model.user.valueobject.LoginIdentifier;
import co.com.makers.model.user.valueobject.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserReactiveRepositoryAdapterTest {

    @Mock
    private UserReactiveRepository repository;

    @Mock
    private ObjectMapper mapper;

    private UserReactiveRepositoryAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new UserReactiveRepositoryAdapter(repository, mapper);
    }

    @Test
    void shouldSaveUserSuccessfully() {
        UUID id = UUID.randomUUID();
        User domainUser = new User(id, Role.ADMIN, new LoginIdentifier("admin@test.com"), "secret", true);
        UserEntity entity = new UserEntity(id, Role.ADMIN, "admin@test.com", "secret", true);

        when(repository.save(any(UserEntity.class))).thenReturn(Mono.just(entity));

        Mono<User> result = adapter.save(domainUser);

        StepVerifier.create(result)
                .expectNextMatches(saved -> saved.id().equals(id)
                        && saved.role() == Role.ADMIN
                        && saved.loginIdentifier().getValue().equals("admin@test.com")
                        && saved.password().equals("secret")
                        && Boolean.TRUE.equals(saved.active()))
                .verifyComplete();

        verify(repository).save(any(UserEntity.class));
    }

    @Test
    void shouldCheckIfUserExistsByLoginIdentifier() {
        LoginIdentifier loginIdentifier = new LoginIdentifier("test@test.com");
        when(repository.existsByLoginIdentifier("test@test.com")).thenReturn(Mono.just(true));

        Mono<Boolean> result = adapter.existsByLoginIdentifier(loginIdentifier);

        StepVerifier.create(result)
                .expectNext(true)
                .verifyComplete();

        verify(repository).existsByLoginIdentifier("test@test.com");
    }

    @Test
    void shouldReturnFalseWhenLoginIdentifierIsNull() {
        Mono<Boolean> result = adapter.existsByLoginIdentifier(null);

        StepVerifier.create(result)
                .expectNext(false)
                .verifyComplete();
    }
}
