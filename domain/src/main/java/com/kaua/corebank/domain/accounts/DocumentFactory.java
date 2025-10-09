package com.kaua.corebank.domain.accounts;

import com.kaua.corebank.domain.accounts.valueobjects.Document;
import com.kaua.corebank.domain.exceptions.DomainException;
import com.kaua.corebank.domain.utils.CpfUtils;

public final class DocumentFactory {

    private DocumentFactory() {}

    public static Document create(final String documentNumber, final String documentType) {
        return switch (documentType.toUpperCase()) {
            case Document.Cpf.DOCUMENT_TYPE -> new Document.Cpf(CpfUtils.cleanCpf(documentNumber));
            default -> throw DomainException.with("Invalid document type");
        };
    }
}
