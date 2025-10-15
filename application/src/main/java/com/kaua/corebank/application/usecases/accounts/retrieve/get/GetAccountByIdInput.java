package com.kaua.corebank.application.usecases.accounts.retrieve.get;

public record GetAccountByIdInput(String accountId) {

    public static GetAccountByIdInput with(final String aAccountId) {
        return new GetAccountByIdInput(aAccountId);
    }
}
