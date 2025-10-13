package com.kaua.corebank.infrastructure.rest.controllers;

import com.kaua.corebank.application.usecases.accounts.create.CreateAccountUseCase;
import com.kaua.corebank.application.usecases.accounts.retrieve.get.GetAccountByIdInput;
import com.kaua.corebank.application.usecases.accounts.retrieve.get.GetAccountByIdUseCase;
import com.kaua.corebank.infrastructure.accounts.req.CreateAccountRequest;
import com.kaua.corebank.infrastructure.accounts.res.CreateAccountResponse;
import com.kaua.corebank.infrastructure.accounts.res.GetAccountByIdResponse;
import com.kaua.corebank.infrastructure.idempotency.IdempotencyKey;
import com.kaua.corebank.infrastructure.rest.AccountAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
public class AccountRestController implements AccountAPI {

    private final Logger log = LoggerFactory.getLogger(AccountRestController.class);

    private final CreateAccountUseCase createAccountUseCase;
    private final GetAccountByIdUseCase getAccountByIdUseCase;

    public AccountRestController(
            final CreateAccountUseCase createAccountUseCase,
            final GetAccountByIdUseCase getAccountByIdUseCase
    ) {
        this.createAccountUseCase = Objects.requireNonNull(createAccountUseCase);
        this.getAccountByIdUseCase = Objects.requireNonNull(getAccountByIdUseCase);
    }

    @IdempotencyKey
    @Override
    public ResponseEntity<CreateAccountResponse> createAccount(final CreateAccountRequest input) {
        final var aInput = input.toInput();

        final var aOutput = this.createAccountUseCase.execute(aInput);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(CreateAccountResponse.from(aOutput));
    }

    @Override
    public ResponseEntity<GetAccountByIdResponse> getAccountById(final String accountId) {
        final var aInput = GetAccountByIdInput.with(accountId);

        final var aOutput = this.getAccountByIdUseCase.execute(aInput);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(GetAccountByIdResponse.from(aOutput));
    }
}
