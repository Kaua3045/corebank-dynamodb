package com.kaua.corebank;

import com.kaua.corebank.infrastructure.configurations.SecurityConfig;
import com.kaua.corebank.infrastructure.idempotency.gateways.InMemoryIdempotencyKeyGateway;
import com.kaua.corebank.infrastructure.wrapper.TracerWrapperOtel;
import org.junit.jupiter.api.Tag;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AliasFor;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@ActiveProfiles("test-integration")
@WebMvcTest
@TestPropertySource(properties = "application.otel.memory-exporter=true")
@Import({SecurityConfig.class, InMemoryIdempotencyKeyGateway.class, ObservationTest.OpenTelemetryTestConfig.class, IntegrationTestConfig.class, TracerWrapperOtel.class})
@Tag("integrationTest")
public @interface ControllerTest {

    @AliasFor(annotation = WebMvcTest.class, attribute = "controllers")
    Class<?>[] controllers() default {};
}
