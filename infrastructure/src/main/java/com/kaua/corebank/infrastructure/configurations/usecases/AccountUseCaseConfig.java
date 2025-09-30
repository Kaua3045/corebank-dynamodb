package com.kaua.corebank.infrastructure.configurations.usecases;

import com.kaua.corebank.application.repositories.AccountRepository;
import com.kaua.corebank.application.usecases.accounts.create.CreateAccountUseCase;
import com.kaua.corebank.application.usecases.accounts.create.DefaultCreateAccountUseCase;
import com.kaua.corebank.application.wrapper.TracerWrapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class AccountUseCaseConfig {

    @Bean
    public CreateAccountUseCase createAccountUseCase(
            final AccountRepository accountRepository,
            final TracerWrapper tracerWrapper
    ) {
        return new DefaultCreateAccountUseCase(
                accountRepository,
                tracerWrapper
        );
    }
}
