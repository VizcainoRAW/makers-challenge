package co.com.makers.model.loanapplication;


import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record LoanApplication (
        UUID id,
        UUID userId,
        BigDecimal amount,
        Instant created_at
){}
