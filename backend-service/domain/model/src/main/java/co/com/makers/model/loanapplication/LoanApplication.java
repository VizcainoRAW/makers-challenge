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
        Instant created_at
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
}
