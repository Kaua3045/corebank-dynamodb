package com.kaua.corebank.domain.accounts.valueobjects;

import com.kaua.corebank.domain.ValueObject;
import com.kaua.corebank.domain.accounts.DocumentFactory;
import com.kaua.corebank.domain.utils.CpfUtils;

public sealed interface Document extends ValueObject {

    String value();

    String formattedValue();

    String type();

    static Document create(final String documentNumber, final String documentType) {
        return DocumentFactory.create(documentNumber, documentType);
    }

    record Cpf(String value) implements Document {
        public static final String DOCUMENT_TYPE = "CPF";

        public Cpf {
            this.assertArgumentNotEmpty(value, "CPF", "should not be empty");
            this.assertArgumentTrue(CpfUtils.validateCpf(value), "CPF", "should be valid");
        }

        @Override
        public String formattedValue() {
            return CpfUtils.formatCpf(value);
        }

        @Override
        public String type() {
            return DOCUMENT_TYPE;
        }
    }
}
