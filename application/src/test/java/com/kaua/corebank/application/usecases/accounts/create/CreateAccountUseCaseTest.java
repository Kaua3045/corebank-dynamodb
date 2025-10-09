package com.kaua.corebank.application.usecases.accounts.create;

import com.kaua.corebank.application.UseCaseTest;
import com.kaua.corebank.application.exceptions.UseCaseInputCannotBeNullException;
import com.kaua.corebank.application.repositories.AccountRepository;
import com.kaua.corebank.domain.exceptions.DomainException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.mockito.AdditionalAnswers.returnsFirstArg;

class CreateAccountUseCaseTest extends UseCaseTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private DefaultCreateAccountUseCase useCase;

    @Test
    void givenAValidCommand_whenCallsCreateAccount_shouldReturnAccountIdAndUserId() {
        final var aFirstName = "Kaua";
        final var aLastName = "Alves";
        final var anEmail = "kaua.alves@mail.com";
        final var aDocumentNumber = "996.207.590-44";
        final var aDocumentType = "CPF";
        final var aUserId = "user-123";

        final var aCommand = CreateAccountInput.with(
                aFirstName,
                aLastName,
                anEmail,
                aDocumentNumber,
                aDocumentType,
                aUserId
        );

        Mockito.when(accountRepository.existsByDocument(aDocumentNumber, aDocumentType))
                .thenReturn(false);
        Mockito.when(accountRepository.existsByEmail(anEmail))
                .thenReturn(false);
        Mockito.when(accountRepository.save(Mockito.any()))
                .thenAnswer(returnsFirstArg());

        final var aActualOutput = this.useCase.execute(aCommand);

        Assertions.assertNotNull(aActualOutput.accountId());
        Assertions.assertEquals(aUserId, aActualOutput.userId());

        Mockito.verify(accountRepository, Mockito.times(1))
                .existsByDocument(aDocumentNumber, aDocumentType);
        Mockito.verify(accountRepository, Mockito.times(1))
                .existsByEmail(anEmail);
        Mockito.verify(accountRepository, Mockito.times(1))
                .save(Mockito.any());
    }

    @Test
    void givenAnInvalidExistsDocument_whenCallsCreateAccount_shouldReturnDomainException() {
        final var aFirstName = "Kaua";
        final var aLastName = "Alves";
        final var anEmail = "kaua.alves@mail.com";
        final var aDocumentNumber = "996.207.590-44";
        final var aDocumentType = "CPF";
        final var aUserId = "user-123";

        final var expectedErrorMessage = "Document %s already exists".formatted(aDocumentNumber);

        final var aCommand = CreateAccountInput.with(
                aFirstName,
                aLastName,
                anEmail,
                aDocumentNumber,
                aDocumentType,
                aUserId
        );

        Mockito.when(accountRepository.existsByDocument(aDocumentNumber, aDocumentType))
                .thenReturn(true);

        final var aActualException = Assertions.assertThrows(
                DomainException.class,
                () -> this.useCase.execute(aCommand)
        );

        Assertions.assertEquals(expectedErrorMessage, aActualException.getMessage());

        Mockito.verify(accountRepository, Mockito.times(1))
                .existsByDocument(aDocumentNumber, aDocumentType);
        Mockito.verify(accountRepository, Mockito.never())
                .existsByEmail(anEmail);
        Mockito.verify(accountRepository, Mockito.never())
                .save(Mockito.any());
    }

    @Test
    void givenAnInvalidExistsEmail_whenCallsCreateAccount_shouldReturnDomainException() {
        final var aFirstName = "Kaua";
        final var aLastName = "Alves";
        final var anEmail = "kaua.alves@mail.com";
        final var aDocumentNumber = "996.207.590-44";
        final var aDocumentType = "CPF";
        final var aUserId = "user-123";

        final var expectedErrorMessage = "Email %s already exists".formatted(anEmail);

        final var aCommand = CreateAccountInput.with(
                aFirstName,
                aLastName,
                anEmail,
                aDocumentNumber,
                aDocumentType,
                aUserId
        );

        Mockito.when(accountRepository.existsByDocument(aDocumentNumber, aDocumentType))
                .thenReturn(false);
        Mockito.when(accountRepository.existsByEmail(anEmail))
                .thenReturn(true);

        final var aActualException = Assertions.assertThrows(
                DomainException.class,
                () -> this.useCase.execute(aCommand)
        );

        Assertions.assertEquals(expectedErrorMessage, aActualException.getMessage());

        Mockito.verify(accountRepository, Mockito.times(1))
                .existsByDocument(aDocumentNumber, aDocumentType);
        Mockito.verify(accountRepository, Mockito.times(1))
                .existsByEmail(anEmail);
        Mockito.verify(accountRepository, Mockito.never())
                .save(Mockito.any());
    }

    @Test
    void givenANullCommand_whenCallsCreateAccount_shouldReturnUseCaseInputCannotBeNullException() {
        final var expectedErrorMessage = "Input to CreateAccountUseCase cannot be null";

        final var aActualException = Assertions.assertThrows(
                UseCaseInputCannotBeNullException.class,
                () -> this.useCase.execute(null)
        );

        Assertions.assertEquals(expectedErrorMessage, aActualException.getMessage());

        Mockito.verify(accountRepository, Mockito.never())
                .existsByDocument(Mockito.any(), Mockito.any());
        Mockito.verify(accountRepository, Mockito.never())
                .existsByEmail(Mockito.any());
        Mockito.verify(accountRepository, Mockito.never())
                .save(Mockito.any());
    }
}
