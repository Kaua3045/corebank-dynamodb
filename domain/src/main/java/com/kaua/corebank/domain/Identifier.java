package com.kaua.corebank.domain;

public interface Identifier<T> extends ValueObject {

    T value();
}
