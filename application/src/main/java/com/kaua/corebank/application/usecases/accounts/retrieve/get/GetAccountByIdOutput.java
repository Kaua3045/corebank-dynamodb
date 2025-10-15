package com.kaua.corebank.application.usecases.accounts.retrieve.get;

import com.kaua.corebank.domain.accounts.Account;

import java.math.BigDecimal;
import java.time.Instant;

public record GetAccountByIdOutput(
        String accountId,
        String userId,
        String firstName,
        String lastName,
        String email,
        String documentNumber,
        String documentType,
        boolean isActive,
        BigDecimal balance,
        Instant createdAt,
        Instant updatedAt,
        long version
) {

    public static GetAccountByIdOutput from(final Account aAccount) {
        return new GetAccountByIdOutput(
                aAccount.getId().value().toString(),
                aAccount.getUserId(),
                aAccount.getName().firstName(),
                aAccount.getName().lastName(),
                aAccount.getEmail().value(),
                aAccount.getDocument().formattedValue(),
                aAccount.getDocument().type(),
                aAccount.isActive(),
                aAccount.getBalance().amount(),
                aAccount.getCreatedAt(),
                aAccount.getUpdatedAt(),
                aAccount.getVersion()
        );
    }
}
