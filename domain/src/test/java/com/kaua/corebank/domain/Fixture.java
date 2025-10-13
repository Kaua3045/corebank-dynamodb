package com.kaua.corebank.domain;

import com.kaua.corebank.domain.accounts.Account;
import com.kaua.corebank.domain.accounts.DocumentFactory;
import com.kaua.corebank.domain.accounts.valueobjects.Email;
import com.kaua.corebank.domain.accounts.valueobjects.Name;
import com.kaua.corebank.domain.utils.IdentifierUtils;
import net.datafaker.Faker;

public final class Fixture {

    private static final Faker faker = new Faker();

    private Fixture() {}

    public static final class AccountFixture {
        private AccountFixture() {}

        public static Account newAccount() {
            final var aFakerFirstName = faker.name().firstName();
            final var aFirstName = aFakerFirstName.length() < 3 || aFakerFirstName.length() > 99 ? "TESSSSS"
                    : aFakerFirstName;
            final var aFakerLastName = faker.name().lastName();
            final var aLastName = aFakerLastName.length() < 3 || aFakerLastName.length() > 99 ?
                    "TESTESSSSS" : aFakerLastName;

            return Account.newAccount(
                    new Name(
                            aFirstName,
                            aLastName
                    ),
                    new Email(faker.internet().emailAddress()),
                    faker.options()
                            .option(
                                    DocumentFactory.create("44822102009", "cpf"),
                                    DocumentFactory.create("01761813005", "cpf"),
                                    DocumentFactory.create("76816398025", "cpf"),
                                    DocumentFactory.create("83869510013", "cpf")
                            ),
                    IdentifierUtils.generateNewMonotonicULID().toString()
            );
        }
    }
}
