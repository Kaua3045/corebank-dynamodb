package com.kaua.corebank.infrastructure.accounts.res;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kaua.corebank.application.usecases.accounts.retrieve.get.GetAccountByIdOutput;

import java.math.BigDecimal;
import java.time.Instant;

public record GetAccountByIdResponse(
        @JsonProperty("account_id") String accountId,
        @JsonProperty("user_id") String userId,
        @JsonProperty("first_name") String firstName,
        @JsonProperty("last_name") String lastName,
        @JsonProperty("email") String email,
        @JsonProperty("document_number") String documentNumber,
        @JsonProperty("document_type") String documentType,
        @JsonProperty("is_active") boolean isActive,
        @JsonProperty("created_at") Instant createdAt,
        @JsonProperty("updated_at") Instant updatedAt,
        @JsonProperty("version") long version
) {

    public static GetAccountByIdResponse from(final GetAccountByIdOutput aOutput) {
        return new GetAccountByIdResponse(
                aOutput.accountId(),
                aOutput.userId(),
                aOutput.firstName(),
                aOutput.lastName(),
                aOutput.email(),
                aOutput.documentNumber(),
                aOutput.documentType(),
                aOutput.isActive(),
                aOutput.createdAt(),
                aOutput.updatedAt(),
                aOutput.version()
        );
    }
}
