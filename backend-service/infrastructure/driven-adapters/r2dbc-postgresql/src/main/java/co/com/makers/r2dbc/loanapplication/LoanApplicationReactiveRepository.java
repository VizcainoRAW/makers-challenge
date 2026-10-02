package co.com.makers.r2dbc.loanapplication;

import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import java.util.UUID;

public interface LoanApplicationReactiveRepository extends ReactiveCrudRepository<LoanApplicationEntity, UUID>, ReactiveQueryByExampleExecutor<LoanApplicationEntity> {

}
