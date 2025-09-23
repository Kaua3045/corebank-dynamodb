package com.kaua.corebank.infrastructure.exceptions;

import com.kaua.corebank.domain.exceptions.NoStackTraceException;

public class IdempotencyKeyUnsupportedMethodException extends NoStackTraceException {

    public IdempotencyKeyUnsupportedMethodException(final String method) {
        super("Idempotency key is not supported for this method: " + method);
    }
}
