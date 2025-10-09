package com.kaua.corebank.infrastructure.configurations;

import com.kaua.corebank.infrastructure.configurations.properties.DynamoDbProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.net.URI;

@Configuration(proxyBeanMethods = false)
public class DynamoDbConfig {

    @Bean
    public DynamoDbClient dynamoDbClient(final DynamoDbProperties properties) {
        return DynamoDbClient.builder()
                .endpointOverride(URI.create(properties.getEndpoint()))
                .region(Region.of(properties.getRegion()))
                .credentialsProvider(StaticCredentialsProvider
                        .create(AwsBasicCredentials
                                .create(properties.getAccessKeyId(), properties.getSecretAccessKey())))
                .build();
    }
}
