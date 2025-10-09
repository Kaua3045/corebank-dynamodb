package com.kaua.corebank.infrastructure.accounts.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kaua.corebank.application.usecases.accounts.create.CreateAccountInput;

public record CreateAccountRequest(
        @JsonProperty("first_name") String firstName,
        @JsonProperty("last_name") String lastName,
        @JsonProperty("email") String email,
        @JsonProperty("document_number") String documentNumber,
        @JsonProperty("document_type") String documentType,
        @JsonProperty("user_id") String userId
) {

    public CreateAccountInput toInput() {
        return new CreateAccountInput(
                firstName(),
                lastName(),
                email(),
                documentNumber(),
                documentType(),
                userId()
        );
    }
}
