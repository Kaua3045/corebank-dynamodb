package com.kaua.corebank.application.usecases.accounts.retrieve.get;

import com.kaua.corebank.application.UseCaseTest;
import com.kaua.corebank.application.exceptions.UseCaseInputCannotBeNullException;
import com.kaua.corebank.application.repositories.AccountRepository;
import com.kaua.corebank.domain.Fixture;
import com.kaua.corebank.domain.exceptions.NotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Optional;

class GetAccountByIdUseCaseTest extends UseCaseTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private DefaultGetAccountByIdUseCase useCase;

    @Test
    void givenAValidId_whenCallsGetAccountById_shouldReturnAccount() {
        final var aAccount = Fixture.AccountFixture.newAccount();
        final var aAccountId = aAccount.getId().value().toString();

        final var aInput = GetAccountByIdInput.with(aAccountId);

        Mockito.when(accountRepository.accountOfId(aAccountId))
                .thenReturn(Optional.of(aAccount));

        final var aOutput = Assertions.assertDoesNotThrow(() -> this.useCase.execute(aInput));

        Assertions.assertNotNull(aOutput);
        Assertions.assertEquals(aAccount.getId().value().toString(), aOutput.accountId());
        Assertions.assertEquals(aAccount.getName().firstName(), aOutput.firstName());
        Assertions.assertEquals(aAccount.getName().lastName(), aOutput.lastName());
        Assertions.assertEquals(aAccount.getEmail().value(), aOutput.email());
        Assertions.assertEquals(aAccount.getDocument().formattedValue(), aOutput.documentNumber());
        Assertions.assertEquals(aAccount.getDocument().type(), aOutput.documentType());
        Assertions.assertEquals(aAccount.getUserId(), aOutput.userId());
        Assertions.assertEquals(aAccount.getCreatedAt(), aOutput.createdAt());
        Assertions.assertEquals(aAccount.getUpdatedAt(), aOutput.updatedAt());
        Assertions.assertEquals(aAccount.isActive(), aOutput.isActive());
        Assertions.assertEquals(aAccount.getBalance().amount(), aOutput.balance());

        Mockito.verify(accountRepository, Mockito.times(1))
                .accountOfId(aAccountId);
    }

    @Test
    void givenAnInvalidId_whenCallsGetAccountById_shouldReturnNotFoundException() {
        final var aAccountId = "invalid-id";

        final var expectedErrorMessage = "Account with id %s was not found".formatted(aAccountId);

        final var aInput = GetAccountByIdInput.with(aAccountId);

        Mockito.when(accountRepository.accountOfId(aAccountId))
                .thenReturn(Optional.empty());

        final var aException = Assertions.assertThrows(
                NotFoundException.class,
                () -> this.useCase.execute(aInput)
        );

        Assertions.assertEquals(expectedErrorMessage, aException.getMessage());

        Mockito.verify(accountRepository, Mockito.times(1))
                .accountOfId(aAccountId);
    }

    @Test
    void givenANullCommand_whenCallsGetAccountById_shouldReturnUseCaseInputCannotBeNullException() {
        final var expectedErrorMessage = "Input to GetAccountByIdUseCase cannot be null";

        final var aActualException = Assertions.assertThrows(
                UseCaseInputCannotBeNullException.class,
                () -> this.useCase.execute(null)
        );

        Assertions.assertEquals(expectedErrorMessage, aActualException.getMessage());

        Mockito.verify(accountRepository, Mockito.never())
                .accountOfId(Mockito.any());
    }
}
