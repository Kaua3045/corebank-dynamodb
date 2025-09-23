package com.kaua.corebank.infrastructure.configurations.authentication;

public record AuthenticatedUser(String id) implements AuthenticatedPrincipal {
}
