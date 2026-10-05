package co.com.makers.api.dto.laonapplication;

import co.com.makers.model.loanapplication.valueobject.LoanStatus;

public record UpdateLoanStatusRequest(
        LoanStatus status
) {}
