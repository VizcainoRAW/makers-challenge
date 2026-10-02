package co.com.makers.api;

import co.com.makers.api.dto.user.CreateUserRequest;
import co.com.makers.api.dto.user.LoginRequest;
import co.com.makers.api.dto.user.UserResponse;
import co.com.makers.model.user.User;
import co.com.makers.model.user.valueobject.LoginIdentifier;
import co.com.makers.model.user.valueobject.Role;
import co.com.makers.usecase.createuser.CreateUserUseCase;
import co.com.makers.usecase.login.LoginUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
class RouterRestTest {

    private WebTestClient webTestClient;

    @Mock
    private CreateUserUseCase createUserUseCase;

    @Mock
    private LoginUseCase loginUseCase;

    @BeforeEach
    void setUp() {
        UserHandler handler = new UserHandler(createUserUseCase, loginUseCase);
        UserRouterRest routerRest = new UserRouterRest();
        webTestClient = WebTestClient.bindToRouterFunction(routerRest.userRouterFunction(handler)).build();
    }

    @Test
    void testCreateUserSuccess() {
        UUID userId = UUID.randomUUID();
        User createdUser = new User(userId, Role.CUSTOMER, new LoginIdentifier("test@example.com"), "secret123", true);

        Mockito.when(createUserUseCase.createUser(eq(Role.CUSTOMER), any(LoginIdentifier.class), eq("secret123")))
                .thenReturn(Mono.just(createdUser));

        CreateUserRequest request = new CreateUserRequest(Role.CUSTOMER, "test@example.com", "secret123");

        webTestClient.post()
                .uri("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserResponse.class)
                .value(response -> {
                    assertThat(response.id()).isEqualTo(userId);
                    assertThat(response.role()).isEqualTo("CUSTOMER");
                    assertThat(response.loginIdentifier()).isEqualTo("test@example.com");
                    assertThat(response.active()).isTrue();
                });
    }

    @Test
    void testLoginSuccess() {
        UUID userId = UUID.randomUUID();
        User loggedUser = new User(userId, Role.CUSTOMER, new LoginIdentifier("test@example.com"), "secret123", true);

        Mockito.when(loginUseCase.login(any(LoginIdentifier.class), eq("secret123")))
                .thenReturn(Mono.just(loggedUser));

        LoginRequest request = new LoginRequest("test@example.com", "secret123");

        webTestClient.post()
                .uri("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserResponse.class)
                .value(response -> {
                    assertThat(response.id()).isEqualTo(userId);
                    assertThat(response.role()).isEqualTo("CUSTOMER");
                    assertThat(response.loginIdentifier()).isEqualTo("test@example.com");
                    assertThat(response.active()).isTrue();
                });
    }
}
