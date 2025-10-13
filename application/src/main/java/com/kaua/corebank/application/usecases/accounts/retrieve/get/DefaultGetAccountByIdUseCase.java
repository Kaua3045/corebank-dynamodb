package com.kaua.corebank.application.usecases.accounts.retrieve.get;

import com.kaua.corebank.application.exceptions.UseCaseInputCannotBeNullException;
import com.kaua.corebank.application.repositories.AccountRepository;
import com.kaua.corebank.domain.accounts.Account;
import com.kaua.corebank.domain.exceptions.NotFoundException;

import java.util.Objects;

public class DefaultGetAccountByIdUseCase extends GetAccountByIdUseCase {

    private final AccountRepository accountRepository;

    public DefaultGetAccountByIdUseCase(final AccountRepository accountRepository) {
        this.accountRepository = Objects.requireNonNull(accountRepository);
    }

    @Override
    public GetAccountByIdOutput execute(final GetAccountByIdInput input) {
        if (input == null) throw new UseCaseInputCannotBeNullException(GetAccountByIdUseCase.class);

        return this.accountRepository.accountOfId(input.accountId())
                .map(GetAccountByIdOutput::from)
                .orElseThrow(NotFoundException.with(Account.class, input.accountId()));
    }
}
