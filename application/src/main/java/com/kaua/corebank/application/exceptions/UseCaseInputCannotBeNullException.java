package com.kaua.corebank.application.exceptions;

import com.kaua.corebank.domain.exceptions.NoStackTraceException;

public class UseCaseInputCannotBeNullException extends NoStackTraceException {

    public UseCaseInputCannotBeNullException(Class<?> useCase) {
        super("Input to %s cannot be null".formatted(useCase.getSimpleName()));
    }
}
