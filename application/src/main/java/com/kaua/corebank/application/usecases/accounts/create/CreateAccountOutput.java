package com.kaua.corebank.application.usecases.accounts.create;

import com.kaua.corebank.domain.accounts.Account;

public record CreateAccountOutput(
        String accountId,
        String userId
) {

    public static CreateAccountOutput from(final Account aAccount) {
        return new CreateAccountOutput(
                aAccount.getId().value().toString(),
                aAccount.getUserId()
        );
    }
}
