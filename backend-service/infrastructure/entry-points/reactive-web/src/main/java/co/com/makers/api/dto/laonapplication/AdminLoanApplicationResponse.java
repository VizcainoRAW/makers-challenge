package co.com.makers.api.dto.laonapplication;

import co.com.makers.model.loanapplication.LoanApplication;
import co.com.makers.model.loanapplication.LoanApplicationDetail;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record AdminLoanApplicationResponse(
        UUID id,
        UUID userId,
        String loginIdentifier,
        String status,
        BigDecimal amount,
        Instant createdAt
) {
    public static AdminLoanApplicationResponse fromDomain(LoanApplicationDetail detail) {
        if (detail == null) return null;
        LoanApplication loan = detail.loanApplication();
        return new AdminLoanApplicationResponse(
                loan.id(),
                loan.userId(),
                detail.loginIdentifier() != null ? detail.loginIdentifier().getValue() : null,
                loan.status() != null ? loan.status().name() : null,
                loan.amount(),
                loan.createdAt()
        );
    }
}
