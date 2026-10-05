package co.com.makers.model.loanapplication;

import co.com.makers.model.user.valueobject.LoginIdentifier;

public record LoanApplicationDetail(
        LoanApplication loanApplication,
        LoginIdentifier loginIdentifier
) {
}
