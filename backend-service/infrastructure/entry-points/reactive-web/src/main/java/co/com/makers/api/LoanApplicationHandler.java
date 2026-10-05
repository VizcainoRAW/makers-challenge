package co.com.makers.api;

import co.com.makers.api.dto.laonapplication.CreateLoanRequest;
import co.com.makers.api.dto.laonapplication.LoanApplicationResponse;
import co.com.makers.usecase.createloanapplication.CreateLoanApplicationUseCase;
import io.netty.channel.unix.Errors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebInputException;
import reactor.core.publisher.Mono;

import javax.xml.validation.Validator;
import java.security.Principal;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class LoanApplicationHandler {

    private final CreateLoanApplicationUseCase createLoanApplicationUseCase;

    public Mono<ServerResponse> createLoanApplication(ServerRequest request) {
        Mono<UUID> userId = request.principal()
                .map(Principal::getName)
                .map(UUID::fromString);

        Mono<CreateLoanRequest> body = request.bodyToMono(CreateLoanRequest.class)
                .switchIfEmpty(Mono.error(new ServerWebInputException("Request body is required")));

        return Mono.zip(userId, body)
                .flatMap(t -> createLoanApplicationUseCase.applyForLoan(t.getT1(), t.getT2().amount()))
                .map(LoanApplicationResponse::fromDomain)
                .flatMap(res -> ServerResponse.status(HttpStatus.CREATED).bodyValue(res));
    }

}
