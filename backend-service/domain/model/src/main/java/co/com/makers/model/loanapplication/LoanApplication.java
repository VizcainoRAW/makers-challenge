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
){}
