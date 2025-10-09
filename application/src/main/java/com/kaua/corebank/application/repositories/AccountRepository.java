package com.kaua.corebank.application.repositories;

import com.kaua.corebank.domain.accounts.Account;

public interface AccountRepository {

    boolean existsByDocument(String document,  String documentType);

    boolean existsByEmail(String email);

    Account save(Account account);
}
