package co.com.makers.api.dto.laonapplication;

import co.com.makers.model.loanapplication.LoanApplication;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record LoanApplicationResponse(
        UUID id,
        UUID userId,
        String status,
        BigDecimal amount,
        Instant createdAt
) {
    public static LoanApplicationResponse fromDomain(LoanApplication loan) {
        if (loan == null) return null;
        return new LoanApplicationResponse(
                loan.id(),
                loan.userId(),
                loan.status() != null ? loan.status().name() : null,
                loan.amount(),
                loan.createdAt()
        );
    }
}