package com.kaua.corebank.domain.accounts;

import com.kaua.corebank.domain.Identifier;
import com.kaua.corebank.domain.utils.ULID;

public record AccountId(ULID value) implements Identifier<ULID> {

    public AccountId {
        this.assertArgumentNotNull(value, "id", "should not be null");
    }
}
