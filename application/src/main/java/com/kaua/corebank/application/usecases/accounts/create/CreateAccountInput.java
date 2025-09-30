package com.kaua.corebank.application.usecases.accounts.create;

public record CreateAccountInput(
        String firstName,
        String lastName,
        String email,
        String documentNumber,
        String documentType,
        String userId
) {

    public static CreateAccountInput with(
            final String aFirstName,
            final String aLastName,
            final String anEmail,
            final String aDocumentNumber,
            final String aDocumentType,
            final String aUserId
    ) {
        return new CreateAccountInput(
                aFirstName,
                aLastName,
                anEmail,
                aDocumentNumber,
                aDocumentType,
                aUserId
        );
    }
}
