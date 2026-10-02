package co.com.makers.usecase.createloanapplication;

import co.com.makers.model.loanapplication.LoanApplication;
import co.com.makers.model.loanapplication.gateways.LoanApplicationRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.UUID;

@RequiredArgsConstructor
public class CreateLoanApplicationUseCase {

    private final LoanApplicationRepository loanApplicationRepository;

    public Mono<LoanApplication> applyForLoan(UUID userId, BigDecimal amount) {
        LoanApplication loanApplication = LoanApplication.create(userId, amount);
        return loanApplicationRepository.save(loanApplication);
    }
}
