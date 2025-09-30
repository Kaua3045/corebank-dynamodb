package com.kaua.corebank.infrastructure.accounts.res;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kaua.corebank.application.usecases.accounts.create.CreateAccountOutput;

public record CreateAccountResponse(
        @JsonProperty("account_id") String accountId,
        @JsonProperty("user_id") String userId
) {

    public static CreateAccountResponse from(final CreateAccountOutput aAccount) {
        return new CreateAccountResponse(
                aAccount.accountId(),
                aAccount.userId()
        );
    }
}
