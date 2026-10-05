package co.com.makers.api;

import co.com.makers.api.dto.laonapplication.AdminLoanApplicationResponse;
import co.com.makers.api.dto.laonapplication.CreateLoanRequest;
import co.com.makers.api.dto.laonapplication.LoanApplicationResponse;
import co.com.makers.api.dto.laonapplication.UpdateLoanStatusRequest;
import co.com.makers.model.loanapplication.exceptions.LoanApplicationNotFoundException;
import co.com.makers.usecase.createloanapplication.CreateLoanApplicationUseCase;
import co.com.makers.usecase.getallloanapplications.GetAllLoanApplicationsUseCase;
import co.com.makers.usecase.updateloanstatus.UpdateLoanStatusUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebInputException;
import reactor.core.publisher.Mono;

import java.security.Principal;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class LoanApplicationHandler {

    private final CreateLoanApplicationUseCase createLoanApplicationUseCase;
    private final GetAllLoanApplicationsUseCase getAllLoanApplicationsUseCase;
    private final UpdateLoanStatusUseCase updateLoanStatusUseCase;

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

    public Mono<ServerResponse> getAllLoanApplications(ServerRequest request) {
        return getAllLoanApplicationsUseCase.getAll()
                .map(AdminLoanApplicationResponse::fromDomain)
                .collectList()
                .flatMap(res -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(res));
    }

    public Mono<ServerResponse> updateLoanStatus(ServerRequest request) {
        UUID loanId;
        try {
            loanId = UUID.fromString(request.pathVariable("id"));
        } catch (IllegalArgumentException e) {
            return ServerResponse.badRequest().bodyValue("Invalid loan id");
        }

        return request.bodyToMono(UpdateLoanStatusRequest.class)
                .filter(body -> body.status() != null)
                .switchIfEmpty(Mono.error(new ServerWebInputException("The loan status is required")))
                .flatMap(body -> updateLoanStatusUseCase.updateStatus(loanId, body.status()))
                .map(LoanApplicationResponse::fromDomain)
                .flatMap(res -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(res))
                .onErrorResume(LoanApplicationNotFoundException.class,
                        ex -> ServerResponse.status(HttpStatus.NOT_FOUND).bodyValue(ex.getMessage()));
    }

}
