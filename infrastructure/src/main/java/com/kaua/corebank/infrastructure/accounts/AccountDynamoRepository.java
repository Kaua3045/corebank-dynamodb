package com.kaua.corebank.infrastructure.accounts;

import com.kaua.corebank.application.repositories.AccountRepository;
import com.kaua.corebank.domain.accounts.Account;
import com.kaua.corebank.domain.accounts.AccountId;
import com.kaua.corebank.domain.accounts.DocumentFactory;
import com.kaua.corebank.domain.accounts.valueobjects.Email;
import com.kaua.corebank.domain.accounts.valueobjects.Name;
import com.kaua.corebank.domain.utils.InstantUtils;
import com.kaua.corebank.domain.utils.ULID;
import com.kaua.corebank.infrastructure.constants.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.*;

@Component
public class AccountDynamoRepository implements AccountRepository {

    private final Logger log = LoggerFactory.getLogger(AccountDynamoRepository.class);

    private final DynamoDbClient dynamoDbClient;

    public AccountDynamoRepository(
            final DynamoDbClient dynamoDbClient
    ) {
        this.dynamoDbClient = Objects.requireNonNull(dynamoDbClient);
    }

    @Override
    public boolean existsByDocument(final String document, final String documentType) {
        String docKey = "ACCOUNT#DOCUMENT#" + documentType + "#" + document;

        QueryResponse response = this.dynamoDbClient.query(QueryRequest.builder()
                .tableName(Constants.DYNAMO_DB_TABLE)
                .keyConditionExpression("PK = :pk")
                .expressionAttributeValues(Map.of(":pk", AttributeValue.fromS(docKey)))
                .limit(1)
                .build());

        return !response.items().isEmpty();
    }

    @Override
    public boolean existsByEmail(final String email) {
        QueryResponse response = this.dynamoDbClient.query(QueryRequest.builder()
                .tableName(Constants.DYNAMO_DB_TABLE)
                .keyConditionExpression("PK = :pk")
                .expressionAttributeValues(Map.of(":pk", AttributeValue.fromS("ACCOUNT#EMAIL#" + email)))
                .limit(1)
                .build());

        return !response.items().isEmpty();
    }

    @Override
    public Optional<Account> accountOfId(final String accountId) {
        GetItemResponse response = this.dynamoDbClient.getItem(GetItemRequest.builder()
                .tableName(Constants.DYNAMO_DB_TABLE)
                .key(Map.of(
                        "PK", AttributeValue.fromS("ACCOUNT#" + accountId),
                        "SK", AttributeValue.fromS("METADATA")
                ))
                .build());

        if (!response.hasItem()) {
            return Optional.empty();
        }

        final var aAccount = mapperToAccount(response);

        return Optional.of(aAccount);
    }

    @Override
    public Account save(final Account account) {
        log.debug("Saving account: {}", account);

        Map<String, AttributeValue> accountItem = new HashMap<>();
        accountItem.put("PK", AttributeValue.fromS("ACCOUNT#" + account.getId().value().toString()));
        accountItem.put("SK", AttributeValue.fromS("METADATA"));
        accountItem.put("Version", AttributeValue.fromN(Long.toString(account.getVersion())));
        accountItem.put("UserId", AttributeValue.fromS(account.getUserId()));
        accountItem.put("FirstName", AttributeValue.fromS(account.getName().firstName()));
        accountItem.put("LastName", AttributeValue.fromS(account.getName().lastName()));
        accountItem.put("Email", AttributeValue.fromS(account.getEmail().value()));
        accountItem.put("DocumentNumber", AttributeValue.fromS(account.getDocument().value()));
        accountItem.put("DocumentType", AttributeValue.fromS(account.getDocument().type()));
        accountItem.put("IsActive", AttributeValue.fromBool(account.isActive()));
        accountItem.put("CreatedAt", AttributeValue.fromS(account.getCreatedAt().toString()));
        accountItem.put("UpdatedAt", AttributeValue.fromS(account.getUpdatedAt().toString()));

        Map<String, AttributeValue> accountEmailRef = new HashMap<>();
        accountEmailRef.put("PK", AttributeValue.fromS("ACCOUNT#EMAIL#" + account.getEmail().value()));
        accountEmailRef.put("SK", AttributeValue.fromS("ACCOUNT#" + account.getId().value().toString()));

        Map<String, AttributeValue> accountDocumentRef = new HashMap<>();
        accountDocumentRef.put("PK", AttributeValue.fromS("ACCOUNT#DOCUMENT#" + account.getDocument().type() + "#" + account.getDocument().value()));
        accountDocumentRef.put("SK", AttributeValue.fromS("ACCOUNT#" + account.getId().value().toString()));

        this.dynamoDbClient.transactWriteItems(TransactWriteItemsRequest.builder()
                .transactItems(
                        List.of(
                                TransactWriteItem.builder().put(Put.builder()
                                                .tableName(Constants.DYNAMO_DB_TABLE).item(accountItem)
                                                .build())
                                        .build(),
                                TransactWriteItem.builder().put(Put.builder()
                                                .tableName(Constants.DYNAMO_DB_TABLE).item(accountEmailRef)
                                                .build())
                                        .build(),
                                TransactWriteItem.builder().put(Put.builder()
                                                .tableName(Constants.DYNAMO_DB_TABLE).item(accountDocumentRef)
                                                .build())
                                        .build()
                        )
                ).build());

        log.info("Account saved: {}", account);
        return account;
    }

    private static Account mapperToAccount(GetItemResponse response) {
        Map<String, AttributeValue> item = response.item();

        return Account.with(
                new AccountId(ULID.fromString(item.get("PK").s().split("#")[1])),
                Long.parseLong(item.get("Version").n()),
                new Name(
                        item.get("FirstName").s(),
                        item.get("LastName").s()
                ),
                new Email(item.get("Email").s()),
                DocumentFactory.create(item.get("DocumentNumber").s(), item.get("DocumentType").s()),
                item.get("UserId").s(),
                item.get("IsActive").bool(),
                InstantUtils.fromString(item.get("CreatedAt").s()).orElse(null),
                InstantUtils.fromString(item.get("UpdatedAt").s()).orElse(null)
        );
    }
}
