package com.kaua.corebank.application.exceptions;

import com.kaua.corebank.domain.exceptions.NoStackTraceException;

public class UseCaseInputCannotBeNullException extends NoStackTraceException {

    public UseCaseInputCannotBeNullException(String useCaseName) {
        super("Input to %s cannot be null".formatted(useCaseName));
    }
}
