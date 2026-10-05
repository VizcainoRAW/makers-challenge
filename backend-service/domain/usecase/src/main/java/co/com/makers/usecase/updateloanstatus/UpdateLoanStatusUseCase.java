package co.com.makers.usecase.updateloanstatus;

import co.com.makers.model.loanapplication.LoanApplication;
import co.com.makers.model.loanapplication.exceptions.LoanApplicationNotFoundException;
import co.com.makers.model.loanapplication.gateways.LoanApplicationRepository;
import co.com.makers.model.loanapplication.valueobject.LoanStatus;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RequiredArgsConstructor
public class UpdateLoanStatusUseCase {

    private final LoanApplicationRepository loanApplicationRepository;

    public Mono<LoanApplication> updateStatus(UUID loanId, LoanStatus newStatus) {
        return loanApplicationRepository.findById(loanId)
                .switchIfEmpty(Mono.error(new LoanApplicationNotFoundException(loanId)))
                .map(loan -> loan.changeStatus(newStatus))
                .flatMap(loanApplicationRepository::update);
    }
}
