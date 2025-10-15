package com.kaua.corebank.application.repositories;

import com.kaua.corebank.domain.accounts.Account;

import java.util.Optional;

public interface AccountRepository {

    boolean existsByDocument(String document,  String documentType);

    boolean existsByEmail(String email);

    Optional<Account> accountOfId(String accountId);

    Account save(Account account);
}
