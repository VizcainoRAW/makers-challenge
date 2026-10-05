package co.com.makers.api;

import co.com.makers.api.dto.laonapplication.CreateLoanRequest;
import co.com.makers.api.dto.laonapplication.LoanApplicationResponse;
import co.com.makers.usecase.createloanapplication.CreateLoanApplicationUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class LoanApplicationHandler {

    private final CreateLoanApplicationUseCase createLoanApplicationUseCase;

    public Mono<ServerResponse> createLoanApplication(ServerRequest request) {
        return request.bodyToMono(CreateLoanRequest.class)
                .flatMap(
                        req -> createLoanApplicationUseCase.applyForLoan(req.userId(), req.amount())
                )
                .map(LoanApplicationResponse::fromDomain)
                .flatMap(res -> ServerResponse.ok().bodyValue(res));
    }
}
