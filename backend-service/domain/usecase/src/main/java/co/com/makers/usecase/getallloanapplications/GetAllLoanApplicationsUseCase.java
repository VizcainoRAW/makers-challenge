package co.com.makers.usecase.getallloanapplications;

import co.com.makers.model.loanapplication.LoanApplication;
import co.com.makers.model.loanapplication.LoanApplicationDetail;
import co.com.makers.model.loanapplication.gateways.LoanApplicationRepository;
import co.com.makers.model.user.User;
import co.com.makers.model.user.gateways.UserRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class GetAllLoanApplicationsUseCase {

    private final LoanApplicationRepository loanApplicationRepository;
    private final UserRepository userRepository;

    public Flux<LoanApplicationDetail> getAll() {
        return loanApplicationRepository.findAll()
                .collectList()
                .flatMapMany(loans -> {
                    Set<UUID> userIds = loans.stream()
                            .map(LoanApplication::userId)
                            .collect(Collectors.toSet());

                    return userRepository.findAllByIds(userIds)
                            .collectMap(User::id)
                            .flatMapMany(users -> Flux.fromIterable(loans)
                                    .map(loan -> {
                                        User user = users.get(loan.userId());
                                        return new LoanApplicationDetail(loan, user != null ? user.loginIdentifier() : null);
                                    }));
                });
    }
}
