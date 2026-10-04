package co.com.makers.api;

import co.com.makers.api.dto.user.CreateUserRequest;
import co.com.makers.api.dto.user.LoginRequest;
import co.com.makers.api.dto.user.UserResponse;
import co.com.makers.model.user.exceptions.UserAlreadyExistsException;
import co.com.makers.model.user.valueobject.LoginIdentifier;
import co.com.makers.usecase.createuser.CreateUserUseCase;
import co.com.makers.usecase.login.LoginUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class UserHandler {

    private final CreateUserUseCase createUser;
    private final LoginUseCase loginUseCase;

    public Mono<ServerResponse> createUser(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(CreateUserRequest.class)
                .flatMap(req -> createUser.createUser(
                        req.role(),
                        new LoginIdentifier(req.loginIdentifier()),
                        req.password()
                ))
                .map(UserResponse::fromDomain)
                .flatMap(res -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(res))
                .onErrorResume(UserAlreadyExistsException.class, ex ->
                        ServerResponse.status(HttpStatus.CONFLICT)
                                .bodyValue(ex.getMessage())
                );
    }

    public Mono<ServerResponse> login(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(LoginRequest.class)
                .flatMap(req -> loginUseCase.login(
                        new LoginIdentifier(req.loginIdentifier()),
                        req.password()
                ))
                .map(UserResponse::fromDomain)
                .flatMap(res -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(res));
    }
}
