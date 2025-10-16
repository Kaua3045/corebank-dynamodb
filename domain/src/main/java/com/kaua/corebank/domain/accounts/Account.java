package com.kaua.corebank.domain.accounts;

import com.kaua.corebank.domain.AggregateRoot;
import com.kaua.corebank.domain.accounts.valueobjects.Document;
import com.kaua.corebank.domain.accounts.valueobjects.Email;
import com.kaua.corebank.domain.accounts.valueobjects.Name;
import com.kaua.corebank.domain.utils.IdentifierUtils;
import com.kaua.corebank.domain.utils.InstantUtils;
import com.kaua.corebank.domain.validation.ValidationHandler;

import java.time.Instant;

public class Account extends AggregateRoot<AccountId> {

    private Name name;
    private Email email;
    private Document document;
    private String userId;
    private boolean isActive;
    private Instant createdAt;
    private Instant updatedAt;

    private Account(
            final AccountId aAccountId,
            final long aVersion,
            final Name aName,
            final Email anEmail,
            final Document aDocument,
            final String aUserId,
            final boolean isActive,
            final Instant aCreatedAt,
            final Instant aUpdatedAt
    ) {
        super(aAccountId, aVersion);
        this.setName(aName);
        this.setEmail(anEmail);
        this.setDocument(aDocument);
        this.setUserId(aUserId);
        this.setActive(isActive);
        this.setCreatedAt(aCreatedAt);
        this.setUpdatedAt(aUpdatedAt);
    }

    public static Account newAccount(
            final Name aName,
            final Email aEmail,
            final Document aDocument,
            final String aUserId
    ) {
        final var aNow = InstantUtils.now();

        return new Account(
                new AccountId(IdentifierUtils.generateNewMonotonicULID()),
                0L,
                aName,
                aEmail,
                aDocument,
                aUserId,
                true,
                aNow,
                aNow
        );
    }

    public static Account with(
            final AccountId aAccountId,
            final long aVersion,
            final Name aName,
            final Email anEmail,
            final Document aDocument,
            final String aUserId,
            final boolean isActive,
            final Instant aCreatedAt,
            final Instant aUpdatedAt
    ) {
        return new Account(
                aAccountId,
                aVersion,
                aName,
                anEmail,
                aDocument,
                aUserId,
                isActive,
                aCreatedAt,
                aUpdatedAt
        );
    }

    public Name getName() {
        return name;
    }

    public Email getEmail() {
        return email;
    }

    public Document getDocument() {
        return document;
    }

    public String getUserId() {
        return userId;
    }

    public boolean isActive() {
        return isActive;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    private void setName(final Name name) {
        this.name = this.assertArgumentNotNull(name, "name", "should not be null");
    }

    private void setEmail(final Email email) {
        this.email = this.assertArgumentNotNull(email, "email", "should not be null");
    }

    private void setDocument(final Document document) {
        this.document = this.assertArgumentNotNull(document, "document", "should not be null");
    }

    private void setUserId(final String userId) {
        this.userId = this.assertArgumentNotEmpty(userId, "userId", "should not be empty");
    }

    private void setActive(final boolean active) {
        this.isActive = active;
    }

    private void setCreatedAt(final Instant createdAt) {
        this.createdAt = this.assertArgumentNotNull(createdAt, "createdAt", "should not be null");
    }

    private void setUpdatedAt(final Instant updatedAt) {
        this.updatedAt = this.assertArgumentNotNull(updatedAt, "updatedAt", "should not be null");
    }

    @Override
    public void validate(ValidationHandler aHandler) {
    }

    @Override
    public String toString() {
        return "Account(" +
                "id=" + getId().value().toString() +
                ", version=" + getVersion() +
                ", name=" + name +
                ", email=" + email + // TODO mask email
                ", document=" + document + // TODO mask document
                ", userId='" + userId + '\'' +
                ", isActive=" + isActive +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ')';
    }
}
