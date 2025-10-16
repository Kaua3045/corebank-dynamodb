package com.kaua.corebank.infrastructure.accounts;

import com.kaua.corebank.AbstractDynamoDbConfig;
import com.kaua.corebank.IntegrationTest;
import com.kaua.corebank.application.repositories.AccountRepository;
import com.kaua.corebank.domain.Fixture;
import com.kaua.corebank.domain.accounts.Account;
import com.kaua.corebank.domain.accounts.DocumentFactory;
import com.kaua.corebank.domain.accounts.valueobjects.Email;
import com.kaua.corebank.domain.accounts.valueobjects.Name;
import com.kaua.corebank.infrastructure.constants.Constants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@IntegrationTest
class AccountDynamoRepositoryTest extends AbstractDynamoDbConfig {

    @Autowired
    private AccountRepository accountRepository;

    @BeforeEach
    void setup() {
        clearDynamoDbTable(Constants.DYNAMO_DB_TABLE);
    }

    @Test
    void givenAValidAccount_whenCallsSave_thenShouldPersistIt() {
        final var aFirstName = "Kaua";
        final var aLastName = "Alves";
        final var anEmail = "kaua.doe@mail.com";
        final var aDocumentNumber = "463.575.460-03";
        final var aDocumentType = "CPF";
        final var aUserId = "user-123";

        final var aName = new Name(aFirstName, aLastName);
        final var aEmail = new Email(anEmail);
        final var aDocument = DocumentFactory.create(aDocumentNumber, aDocumentType);

        final var aAccount = Account.newAccount(
                aName,
                aEmail,
                aDocument,
                aUserId
        );

        final var aSavedAccount = Assertions.assertDoesNotThrow(() -> this.accountRepository.save(aAccount));

        Assertions.assertNotNull(aSavedAccount);
        Assertions.assertEquals(aAccount.getId(), aSavedAccount.getId());
        Assertions.assertEquals(aAccount.getName(), aSavedAccount.getName());
        Assertions.assertEquals(aAccount.getEmail(), aSavedAccount.getEmail());
        Assertions.assertEquals(aAccount.getDocument(), aSavedAccount.getDocument());
        Assertions.assertEquals(aAccount.getUserId(), aSavedAccount.getUserId());
        Assertions.assertEquals(aAccount.isActive(), aSavedAccount.isActive());
        Assertions.assertEquals(aAccount.getCreatedAt(), aSavedAccount.getCreatedAt());
        Assertions.assertEquals(aAccount.getUpdatedAt(), aSavedAccount.getUpdatedAt());
    }

    @Test
    void givenAnExistingEmail_whenCallsExistsByEmail_thenShouldReturnTrue() {
        final var aFirstName = "Kaua";
        final var aLastName = "Alves";
        final var anEmail = "kaua.doe@mail.com";
        final var aDocumentNumber = "463.575.460-03";
        final var aDocumentType = "CPF";
        final var aUserId = "user-123";

        final var aName = new Name(aFirstName, aLastName);
        final var aEmail = new Email(anEmail);
        final var aDocument = DocumentFactory.create(aDocumentNumber, aDocumentType);

        final var aAccount = Account.newAccount(
                aName,
                aEmail,
                aDocument,
                aUserId
        );

        Assertions.assertDoesNotThrow(() -> this.accountRepository.save(aAccount));

        final var existsByEmail = Assertions.assertDoesNotThrow(() -> this.accountRepository.existsByEmail(anEmail));

        Assertions.assertTrue(existsByEmail);
    }

    @Test
    void givenANonExistingEmail_whenCallsExistsByEmail_thenShouldReturnFalse() {
        final var anEmail = "kaua.doe@mail.com";

        final var existsByEmail = Assertions.assertDoesNotThrow(() -> this.accountRepository.existsByEmail(anEmail));

        Assertions.assertFalse(existsByEmail);
    }

    @Test
    void givenAnExistingDocument_whenCallsExistsByDocument_thenShouldReturnTrue() {
        final var aFirstName = "Kaua";
        final var aLastName = "Alves";
        final var anEmail = "kaua.doe@mail.com";
        final var aDocumentNumber = "46357546003";
        final var aDocumentType = "CPF";
        final var aUserId = "user-123";

        final var aName = new Name(aFirstName, aLastName);
        final var aEmail = new Email(anEmail);
        final var aDocument = DocumentFactory.create(aDocumentNumber, aDocumentType);

        final var aAccount = Account.newAccount(
                aName,
                aEmail,
                aDocument,
                aUserId
        );

        Assertions.assertDoesNotThrow(() -> this.accountRepository.save(aAccount));

        final var existsByDocument = Assertions.assertDoesNotThrow(() -> this.accountRepository.existsByDocument(aDocumentNumber, aDocumentType));

        Assertions.assertTrue(existsByDocument);
    }

    @Test
    void givenANonExistingDocument_whenCallsExistsByDocument_thenShouldReturnFalse() {
        final var aDocumentNumber = "46357546005";
        final var aDocumentType = "CPF";

        final var existsByDocument = Assertions.assertDoesNotThrow(() -> this.accountRepository.existsByDocument(aDocumentNumber, aDocumentType));

        Assertions.assertFalse(existsByDocument);
    }

    @Test
    void givenAnExistingAccountId_whenCallsAccountOfId_thenShouldReturnAccount() {
        final var aAccount = Fixture.AccountFixture.newAccount();
        final var aAccountId = aAccount.getId().value().toString();

        Assertions.assertDoesNotThrow(() -> this.accountRepository.save(aAccount));

        final var aRetrievedAccount = Assertions.assertDoesNotThrow(() -> this.accountRepository.accountOfId(aAccountId));

        Assertions.assertTrue(aRetrievedAccount.isPresent());
        Assertions.assertEquals(aAccountId, aRetrievedAccount.get().getId().value().toString());
        Assertions.assertEquals(aAccount.getName(), aRetrievedAccount.get().getName());
        Assertions.assertEquals(aAccount.getEmail(), aRetrievedAccount.get().getEmail());
        Assertions.assertEquals(aAccount.getDocument(), aRetrievedAccount.get().getDocument());
        Assertions.assertEquals(aAccount.getUserId(), aRetrievedAccount.get().getUserId());
        Assertions.assertEquals(aAccount.isActive(), aRetrievedAccount.get().isActive());
        Assertions.assertEquals(aAccount.getCreatedAt(), aRetrievedAccount.get().getCreatedAt());
        Assertions.assertEquals(aAccount.getUpdatedAt(), aRetrievedAccount.get().getUpdatedAt());
        Assertions.assertEquals(aAccount.getVersion(), aRetrievedAccount.get().getVersion());
    }

    @Test
    void givenANonExistingAccountId_whenCallsAccountOfId_thenShouldReturnEmpty() {
        final var aAccountId = "non-exists";

        final var aRetrievedAccount = Assertions.assertDoesNotThrow(() -> this.accountRepository.accountOfId(aAccountId));

        Assertions.assertTrue(aRetrievedAccount.isEmpty());
    }
}
