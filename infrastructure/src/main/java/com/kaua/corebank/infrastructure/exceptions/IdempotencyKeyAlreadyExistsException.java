package com.kaua.corebank.infrastructure.exceptions;

import com.kaua.corebank.domain.exceptions.DomainException;

import java.util.Collections;

public class IdempotencyKeyAlreadyExistsException extends DomainException {

    public IdempotencyKeyAlreadyExistsException() {
        super("Idempotency key already exists", Collections.emptyList());
    }
}
