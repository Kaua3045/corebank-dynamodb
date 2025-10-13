package com.kaua.corebank.infrastructure.rest;

import com.kaua.corebank.infrastructure.accounts.req.CreateAccountRequest;
import com.kaua.corebank.infrastructure.accounts.res.CreateAccountResponse;
import com.kaua.corebank.infrastructure.accounts.res.GetAccountByIdResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Accounts", description = "Account management API")
@RequestMapping("/v1/accounts")
public interface AccountAPI {

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Create a new account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Account successfully created"),
            @ApiResponse(responseCode = "400", description = "A validation error was observed"),
            @ApiResponse(responseCode = "422", description = "A business rule was violated"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    ResponseEntity<CreateAccountResponse> createAccount(@RequestBody CreateAccountRequest input);

    @GetMapping(
            path = "/{accountId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Get account by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Account successfully retrieved"),
            @ApiResponse(responseCode = "400", description = "A validation error was observed"),
            @ApiResponse(responseCode = "404", description = "Account not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    ResponseEntity<GetAccountByIdResponse> getAccountById(@PathVariable(name = "accountId") String accountId);
}
