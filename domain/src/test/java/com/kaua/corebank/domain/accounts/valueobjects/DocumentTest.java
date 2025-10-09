package com.kaua.corebank.domain.accounts.valueobjects;

import com.kaua.corebank.domain.UnitTest;
import com.kaua.corebank.domain.exceptions.DomainException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class DocumentTest extends UnitTest {

    @Test
    void givenAValidDocumentNumberAndCpfType_whenCreateDocument_thenDocumentIsCreated() {
        final var aType = Document.Cpf.DOCUMENT_TYPE;
        final var aNumber = "479.993.810-04";
        final var aNumberClean = "47999381004";

        final var aDocument = Document.create(aNumber, aType);

        Assertions.assertEquals(aNumber, aDocument.formattedValue());
        Assertions.assertEquals(aType, aDocument.type());
        Assertions.assertEquals(aNumberClean, aDocument.value());
    }

    @Test
    void givenAnInvalidDocumentType_whenCreateDocument_thenThrowDomainException() {
        final var aType = "INVALID";
        final var aNumber = "479.993.810-04";

        final var aMessage = "Invalid document type";

        final var aException = Assertions.assertThrows(DomainException.class,
                () -> Document.create(aNumber, aType));

        Assertions.assertEquals(aMessage, aException.getMessage());
    }
}
