package co.com.makers.model.loanapplication;


import co.com.makers.model.loanapplication.valueobject.LoanStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record LoanApplication (
        UUID id,
        UUID userId,
        LoanStatus status,
        BigDecimal amount,
        Instant createdAt
){

    public static LoanApplication create(UUID userId, BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("The loan amount must be greater than zero");
        }

        return new LoanApplication(
                UUID.randomUUID(),
                userId,
                LoanStatus.PENDING,
                amount,
                Instant.now()
        );
    }

    public LoanApplication changeStatus(LoanStatus newStatus) {
        if (newStatus == null) {
            throw new IllegalArgumentException("The loan status is required");
        }
        return new LoanApplication(id, userId, newStatus, amount, createdAt);
    }
}
