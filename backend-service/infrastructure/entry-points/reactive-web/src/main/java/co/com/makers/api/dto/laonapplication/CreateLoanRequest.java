package co.com.makers.api.dto.laonapplication;

import java.math.BigDecimal;

public record CreateLoanRequest(
       BigDecimal amount
) {
    public CreateLoanRequest(BigDecimal amount){
        if(amount == null || amount.compareTo(BigDecimal.ZERO) <= 0){
            throw new IllegalArgumentException("Amount must be greater than 0");
        }
        this.amount = amount;
    }
}