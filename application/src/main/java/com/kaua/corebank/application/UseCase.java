package com.kaua.corebank.application;

public abstract class UseCase<I, O> {

    public abstract O execute(I input);
}
