package com.kaua.corebank.infrastructure.configurations.authentication;

public sealed interface AuthenticatedPrincipal permits AuthenticatedService, AuthenticatedUser {

    String id();
}
