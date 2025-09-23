package com.kaua.corebank.infrastructure.configurations.authentication;

public record AuthenticatedService(String id) implements AuthenticatedPrincipal {
}
