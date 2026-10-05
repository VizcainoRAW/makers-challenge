package co.com.makers.model.loanapplication.exceptions;

import java.util.UUID;

public class LoanApplicationNotFoundException extends RuntimeException {

    public LoanApplicationNotFoundException(UUID id) {
        super("the loan application " + id + " was not found");
    }
}
