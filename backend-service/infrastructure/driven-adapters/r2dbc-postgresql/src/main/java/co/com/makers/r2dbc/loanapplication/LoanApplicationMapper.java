package co.com.makers.r2dbc.loanapplication;

import co.com.makers.model.loanapplication.LoanApplication;

public class LoanApplicationMapper {

    public static LoanApplication toDomain(LoanApplicationEntity entity) {
        if (entity == null) return null;
        return new LoanApplication(
                entity.getId(),
                entity.getUserId(),
                entity.getStatus(),
                entity.getAmount(),
                entity.getCreatedAt()
        );
    }

    public static LoanApplicationEntity toEntity(LoanApplication domain) {
        if (domain == null) return null;
        return new LoanApplicationEntity(
                domain.id(),
                domain.userId(),
                domain.status(),
                domain.amount(),
                domain.createdAt()
        );
    }
}