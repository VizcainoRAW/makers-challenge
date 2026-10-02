package co.com.makers.r2dbc.loanapplication;

import co.com.makers.model.loanapplication.LoanApplication;
import co.com.makers.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class LoanApplicationReactiveRepositoryAdapter extends ReactiveAdapterOperations<
        LoanApplication/* change for domain model */,
    LoanApplicationEntity/* change for adapter model */,
        UUID,
        LoanApplicationReactiveRepository
> {
    public LoanApplicationReactiveRepositoryAdapter(LoanApplicationReactiveRepository repository, ObjectMapper mapper) {
        /**
         *  Could be use mapper.mapBuilder if your domain model implement builder pattern
         *  super(repository, mapper, d -> mapper.mapBuilder(d,ObjectModel.ObjectModelBuilder.class).build());
         *  Or using mapper.map with the class of the object model
         */
        super(repository, mapper, LoanApplicationMapper::toDomain);
    }

    @Override
    protected LoanApplicationEntity toData(LoanApplication entity) {
        return LoanApplicationMapper.toEntity(entity);
    }
}
