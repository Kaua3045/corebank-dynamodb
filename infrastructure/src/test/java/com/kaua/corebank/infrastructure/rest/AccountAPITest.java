package com.kaua.corebank.infrastructure.rest;

import com.kaua.corebank.ApiTest;
import com.kaua.corebank.ControllerTest;
import com.kaua.corebank.application.usecases.accounts.create.CreateAccountOutput;
import com.kaua.corebank.application.usecases.accounts.create.CreateAccountUseCase;
import com.kaua.corebank.domain.utils.ULID;
import com.kaua.corebank.infrastructure.idempotency.IdempotencyKey;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ControllerTest(controllers = AccountAPI.class)
class AccountAPITest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private CreateAccountUseCase createAccountUseCase;

    @Test
    void givenAValidRequest_whenCallsCreateAccount_thenShouldReturn201AndAccountId() throws Exception {
        final var aFirstName = "Kaua";
        final var aLastName = "Alves";
        final var anEmail = "kaua.doe@mail.com";
        final var aDocumentNumber = "463.575.460-03";
        final var aDocumentType = "CPF";
        final var aUserId = "user-123";

        final var expectedAccountId = "account-123";

        Mockito.when(createAccountUseCase.execute(Mockito.any()))
                .thenAnswer(call -> new CreateAccountOutput(expectedAccountId, aUserId));

        var json = """
                {
                    "first_name": "%s",
                    "last_name": "%s",
                    "email": "%s",
                    "document_number": "%s",
                    "document_type": "%s",
                    "user_id": "%s"
                }
                """.formatted(
                aFirstName,
                aLastName,
                anEmail,
                aDocumentNumber,
                aDocumentType,
                aUserId
        );

        final var aRequest = post("/v1/accounts")
                .with(ApiTest.admin())
                .with(csrf())
                .header(IdempotencyKey.IDEMPOTENCY_KEY_HEADER, ULID.random().toString())
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(json);

        final var aResponse = this.mvc.perform(aRequest);

        aResponse
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isCreated())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.account_id").value(expectedAccountId))
                .andExpect(jsonPath("$.user_id").value(aUserId));

        Mockito.verify(createAccountUseCase, Mockito.times(1)).execute(Mockito.any());
    }
}
