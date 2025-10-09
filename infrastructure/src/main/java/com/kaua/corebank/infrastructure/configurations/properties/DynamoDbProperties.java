package com.kaua.corebank.infrastructure.configurations.properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@ConfigurationProperties(prefix = "application.dynamodb")
public class DynamoDbProperties implements InitializingBean {

    private static final Logger log = LoggerFactory.getLogger(DynamoDbProperties.class);

    private boolean isLocal;
    private String endpoint;
    private String region;
    private String accessKeyId;
    private String secretAccessKey;

    @Override
    public void afterPropertiesSet() throws Exception {
        log.debug("DynamoDbProperties initialized with values: {}", this);
    }

    public boolean isLocal() {
        return isLocal;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public String getRegion() {
        return region;
    }

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public String getSecretAccessKey() {
        return secretAccessKey;
    }

    public void setLocal(boolean local) {
        isLocal = local;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    public void setSecretAccessKey(String secretAccessKey) {
        this.secretAccessKey = secretAccessKey;
    }

    @Override
    public String toString() {
        return "DynamoDbProperties(" +
                "isLocal=" + isLocal +
                ", endpoint='" + endpoint + '\'' +
                ", region='" + region + '\'' +
                ", accessKeyId='" + (isLocal() ? accessKeyId : "confidential") + '\'' +
                ", secretAccessKey='" + (isLocal() ? secretAccessKey : "confidential") + '\'' +
                ')';
    }
}
