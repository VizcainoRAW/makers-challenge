package co.com.makers.api.dto.laonapplication;

import java.math.BigDecimal;
import java.util.UUID;

public record CreateLoanRequest(
        UUID userId,
        BigDecimal amount
) {}