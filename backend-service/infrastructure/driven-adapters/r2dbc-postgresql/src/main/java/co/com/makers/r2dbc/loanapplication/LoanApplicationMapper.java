package co.com.makers.r2dbc.loanapplication;

import co.com.makers.model.loanapplication.LoanApplication;
import co.com.makers.model.loanapplication.valueobject.LoanStatus;

public class LoanApplicationMapper {

    public static LoanApplication toDomain(LoanApplicationEntity entity) {
        if (entity == null) return null;
        LoanStatus status = entity.getStatus() != null ? LoanStatus.valueOf(entity.getStatus()) : null;
        return new LoanApplication(
                entity.getId(),
                entity.getUserId(),
                status,
                entity.getAmount(),
                entity.getCreatedAt()
        );
    }

    public static LoanApplicationEntity toEntity(LoanApplication domain) {
        if (domain == null) return null;
        LoanApplicationEntity entity = new LoanApplicationEntity(
                domain.id(),
                domain.userId(),
                domain.status(),
                domain.amount(),
                domain.createdAt()
        );
        entity.setNew(true);
        return entity;
    }
}