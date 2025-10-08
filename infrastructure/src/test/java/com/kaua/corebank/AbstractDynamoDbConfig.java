package com.kaua.corebank;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.Map;

@Testcontainers
@DirtiesContext
public abstract class AbstractDynamoDbConfig {

    @Container
    static final LocalStackContainer localstack = new LocalStackContainer(
            DockerImageName.parse("localstack/localstack:2.2.0"))
            .withServices(LocalStackContainer.Service.DYNAMODB);

    private static final DynamoDbClient dynamoDbClient;

    private static final Logger log = LoggerFactory.getLogger(AbstractDynamoDbConfig.class);

    static {
        localstack.start();

        dynamoDbClient = DynamoDbClient.builder()
                .endpointOverride(localstack.getEndpointOverride(LocalStackContainer.Service.DYNAMODB))
                .region(Region.of(localstack.getRegion()))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(localstack.getAccessKey(), localstack.getSecretKey())
                ))
                .build();

        createTableIfNotExists("corebank-table");
    }

    @DynamicPropertySource
    public static void dynamoDbProperties(final DynamicPropertyRegistry registry) {
        String endpoint = localstack.getEndpointOverride(LocalStackContainer.Service.DYNAMODB).toString();

        registry.add("application.dynamodb.endpoint", () -> endpoint);
        registry.add("application.dynamodb.region", localstack::getRegion);
        registry.add("application.dynamodb.accessKeyId", localstack::getAccessKey);
        registry.add("application.dynamodb.secretAccessKey", localstack::getSecretKey);
    }

    protected void clearDynamoDbTable(final String tableName) {
        log.debug("Clearing DynamoDB table: {}", tableName);
        ScanResponse scanResponse = dynamoDbClient.scan(ScanRequest.builder()
                .tableName(tableName)
                .attributesToGet("PK", "SK")
                .build());

        log.debug("Found {} items to delete", scanResponse.count());

        if (scanResponse.count() == 0) {
            return;
        }

        for (Map<String, AttributeValue> item : scanResponse.items()) {
            dynamoDbClient.deleteItem(DeleteItemRequest.builder()
                    .tableName(tableName)
                    .key(Map.of(
                            "PK", item.get("PK"),
                            "SK", item.get("SK")
                    ))
                    .build());
            log.warn("Deleted item with PK: {}, SK: {}", item.get("PK").s(), item.get("SK").s());
        }
    }

    private static void createTableIfNotExists(final String tableName) {
        try {
            dynamoDbClient.describeTable(DescribeTableRequest.builder()
                    .tableName(tableName)
                    .build());
            log.warn("Table {} already exists", tableName);
        } catch (ResourceNotFoundException e) {
            dynamoDbClient.createTable(CreateTableRequest.builder()
                    .tableName(tableName)
                    .keySchema(
                            KeySchemaElement.builder()
                                    .attributeName("PK")
                                    .keyType(KeyType.HASH)
                                    .build(),
                            KeySchemaElement.builder()
                                    .attributeName("SK")
                                    .keyType(KeyType.RANGE)
                                    .build()
                    )
                    .attributeDefinitions(
                            AttributeDefinition.builder()
                                    .attributeName("PK")
                                    .attributeType(ScalarAttributeType.S)
                                    .build(),
                            AttributeDefinition.builder()
                                    .attributeName("SK")
                                    .attributeType(ScalarAttributeType.S)
                                    .build()
                    )
                    .billingMode(BillingMode.PAY_PER_REQUEST)
                    .build());
            log.info("Table {} created", tableName);
        }
    }
}
