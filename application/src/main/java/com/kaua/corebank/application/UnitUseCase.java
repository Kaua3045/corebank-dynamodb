package com.kaua.corebank.application;

public abstract class UnitUseCase<I> {

    public abstract void execute(I input);
}
