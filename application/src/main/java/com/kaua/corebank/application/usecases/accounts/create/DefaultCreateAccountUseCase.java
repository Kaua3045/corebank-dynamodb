package com.kaua.corebank.application.usecases.accounts.create;

import com.kaua.corebank.application.exceptions.UseCaseInputCannotBeNullException;
import com.kaua.corebank.application.repositories.AccountRepository;
import com.kaua.corebank.application.wrapper.TracerWrapper;
import com.kaua.corebank.domain.accounts.Account;
import com.kaua.corebank.domain.accounts.DocumentFactory;
import com.kaua.corebank.domain.accounts.valueobjects.Email;
import com.kaua.corebank.domain.accounts.valueobjects.Name;
import com.kaua.corebank.domain.exceptions.DomainException;

import java.util.Objects;

public class DefaultCreateAccountUseCase extends CreateAccountUseCase {

    private final AccountRepository accountRepository;
    private final TracerWrapper tracerWrapper;

    public DefaultCreateAccountUseCase(
            final AccountRepository accountRepository,
            final TracerWrapper tracerWrapper
    ) {
        this.accountRepository = Objects.requireNonNull(accountRepository);
        this.tracerWrapper = Objects.requireNonNull(tracerWrapper);
    }

    @Override
    public CreateAccountOutput execute(final CreateAccountInput input) {
        return this.tracerWrapper.traceWithReturn(
                "createAccountUseCase",
                (ctx) -> {
                    if (input == null) throw new UseCaseInputCannotBeNullException(CreateAccountUseCase.class);

                    final var aExistsByDocument = ctx.runInSpan(
                            "account.existsByDocument",
                            () -> this.accountRepository.existsByDocument(
                                    input.documentNumber(),
                                    input.documentType()
                            )
                    );

                    if (aExistsByDocument) {
                        throw DomainException.with("Document %s already exists".formatted(input.documentNumber()));
                    }

                    final var aExistsByEmail = ctx.runInSpan(
                            "account.existsByEmail",
                            () -> this.accountRepository.existsByEmail(input.email())
                    );

                    if (aExistsByEmail) {
                        throw DomainException.with("Email %s already exists".formatted(input.email()));
                    }

                    final var aName = new Name(input.firstName(), input.lastName());
                    final var aEmail = new Email(input.email());
                    final var aDocument = DocumentFactory.create(input.documentNumber(), input.documentType());

                    final var aAccount = Account.newAccount(
                            aName,
                            aEmail,
                            aDocument,
                            input.userId()
                    );

                    ctx.setAttribute("account.id", aAccount.getId().value().toString());
                    ctx.setAttribute("account.userId", aAccount.getUserId());

                    ctx.runInSpan(
                            "account.save",
                            () -> this.accountRepository.save(aAccount)
                    );

                    return CreateAccountOutput.from(aAccount);
                }
        );
    }
}
