package com.kaua.corebank.domain.accounts;

import com.kaua.corebank.domain.UnitTest;
import com.kaua.corebank.domain.accounts.valueobjects.Email;
import com.kaua.corebank.domain.accounts.valueobjects.Name;
import com.kaua.corebank.domain.utils.IdentifierUtils;
import com.kaua.corebank.domain.utils.InstantUtils;
import com.kaua.corebank.domain.validation.handler.NotificationHandler;
import com.kaua.corebank.domain.valueobjects.Money;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

class AccountTest extends UnitTest {

    @Test
    void givenAValidParams_whenCreateAccount_thenInstantiateAnAccount() {
        final var aFirstName = "Kaua";
        final var aLastName = "Alves";
        final var aEmail = "kaua.alves@mail.com";
        final var aDocumentNumber = "479.993.810-04";
        final var aDocumentType = "CPF";
        final var aUserId = "user-123";

        final var aName = new Name(aFirstName, aLastName);
        final var anEmail = new Email(aEmail);
        final var aDocument = DocumentFactory.create(aDocumentNumber, aDocumentType);

        final var anAccount = Account.newAccount(
                aName,
                anEmail,
                aDocument,
                aUserId
        );

        Assertions.assertNotNull(anAccount);
        Assertions.assertNotNull(anAccount.getId());
        Assertions.assertEquals(0, anAccount.getVersion());
        Assertions.assertEquals(aFirstName, anAccount.getName().firstName());
        Assertions.assertEquals(aLastName, anAccount.getName().lastName());
        Assertions.assertEquals(aEmail, anAccount.getEmail().value());
        Assertions.assertEquals(aDocumentNumber, anAccount.getDocument().formattedValue());
        Assertions.assertEquals(aDocumentType, anAccount.getDocument().type());
        Assertions.assertEquals(aDocument.value(), anAccount.getDocument().value());
        Assertions.assertEquals(aUserId, anAccount.getUserId());
        Assertions.assertTrue(anAccount.isActive());
        Assertions.assertNotNull(anAccount.getCreatedAt());
        Assertions.assertNotNull(anAccount.getUpdatedAt());
        Assertions.assertDoesNotThrow(() -> anAccount.validate(NotificationHandler.create()));
    }

    @Test
    void givenAValidParams_whenCallWith_thenReturnAAccount() {
        final var aId = new AccountId(IdentifierUtils.generateNewMonotonicULID());
        final var aVersion = 1L;
        final var aFirstName = "Kaua";
        final var aLastName = "Alves";
        final var aEmail = "kaua.alves@mail.com";
        final var aDocumentNumber = "479.993.810-04";
        final var aDocumentType = "CPF";
        final var aUserId = "user-123";
        final var isActive = true;
        final var aBalance = 100.0;
        final var aNow = InstantUtils.now();

        final var aName = new Name(aFirstName, aLastName);
        final var anEmail = new Email(aEmail);
        final var aDocument = DocumentFactory.create(aDocumentNumber, aDocumentType);

        final var anAccount = Account.with(
                aId,
                aVersion,
                aName,
                anEmail,
                aDocument,
                aUserId,
                isActive,
                aNow,
                aNow
        );

        Assertions.assertNotNull(anAccount);
        Assertions.assertEquals(aId, anAccount.getId());
        Assertions.assertEquals(aVersion, anAccount.getVersion());
        Assertions.assertEquals(aFirstName, anAccount.getName().firstName());
        Assertions.assertEquals(aLastName, anAccount.getName().lastName());
        Assertions.assertEquals(aEmail, anAccount.getEmail().value());
        Assertions.assertEquals(aDocumentNumber, anAccount.getDocument().formattedValue());
        Assertions.assertEquals(aDocumentType, anAccount.getDocument().type());
        Assertions.assertEquals(aDocument.value(), anAccount.getDocument().value());
        Assertions.assertEquals(aUserId, anAccount.getUserId());
        Assertions.assertTrue(anAccount.isActive());
        Assertions.assertEquals(aNow, anAccount.getCreatedAt());
        Assertions.assertEquals(aNow, anAccount.getUpdatedAt());
        Assertions.assertDoesNotThrow(() -> anAccount.validate(NotificationHandler.create()));
    }

    @Test
    void testCallAccountToString() {
        final var aFirstName = "Kaua";
        final var aLastName = "Alves";
        final var aEmail = "kaua.alves@mail.com";
        final var aDocumentNumber = "479.993.810-04";
        final var aDocumentType = "CPF";
        final var aUserId = "user-123";

        final var aName = new Name(aFirstName, aLastName);
        final var anEmail = new Email(aEmail);
        final var aDocument = DocumentFactory.create(aDocumentNumber, aDocumentType);

        final var anAccount = Account.newAccount(
                aName,
                anEmail,
                aDocument,
                aUserId
        );

        final var expectedString = "Account(" +
                "id=" + anAccount.getId().value().toString() +
                ", version=" + anAccount.getVersion() +
                ", name=" + anAccount.getName() +
                ", email=" + anAccount.getEmail() +
                ", document=" + anAccount.getDocument() +
                ", userId='" + anAccount.getUserId() + '\'' +
                ", isActive=" + anAccount.isActive() +
                ", createdAt=" + anAccount.getCreatedAt() +
                ", updatedAt=" + anAccount.getUpdatedAt() +
                ')';

        Assertions.assertEquals(expectedString, anAccount.toString());
    }
}
