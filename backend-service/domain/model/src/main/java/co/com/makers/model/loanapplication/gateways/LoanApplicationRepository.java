package co.com.makers.model.loanapplication.gateways;

import co.com.makers.model.loanapplication.LoanApplication;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface LoanApplicationRepository {
    Mono<LoanApplication> save(LoanApplication loanApplication);

    Mono<LoanApplication> update(LoanApplication loanApplication);

    Mono<LoanApplication> findById(UUID id);

    Flux<LoanApplication> findAll();
}
