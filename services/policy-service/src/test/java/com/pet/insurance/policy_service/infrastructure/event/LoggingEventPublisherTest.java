package com.pet.insurance.policy_service.infrastructure.event;

import com.pet.insurance.policy_service.domain.event.PolicyIssuedEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("LoggingEventPublisher Tests")
class LoggingEventPublisherTest {

    private LoggingEventPublisher publisher;

    @BeforeEach
    void setUp() {
        publisher = new LoggingEventPublisher();
    }

    @Test
    @DisplayName("should successfully publish PolicyIssuedEvent")
    void shouldPublishPolicyIssuedEvent() {
        // Given
        UUID policyId = UUID.randomUUID();
        UUID quotationId = UUID.randomUUID();
        String ownerEmail = "test@example.com";
        PolicyIssuedEvent event = new PolicyIssuedEvent(policyId, quotationId, ownerEmail);

        // When
        Mono<Void> result = publisher.publishPolicyIssued(event);

        // Then
        StepVerifier.create(result)
                .verifyComplete();
    }

    @Test
    @DisplayName("should handle null email in event")
    void shouldHandleNullEmail() {
        // Given
        UUID policyId = UUID.randomUUID();
        UUID quotationId = UUID.randomUUID();
        PolicyIssuedEvent event = new PolicyIssuedEvent(policyId, quotationId, null);

        // When
        Mono<Void> result = publisher.publishPolicyIssued(event);

        // Then
        StepVerifier.create(result)
                .verifyComplete();
    }

    @Test
    @DisplayName("should handle empty email in event")
    void shouldHandleEmptyEmail() {
        // Given
        UUID policyId = UUID.randomUUID();
        UUID quotationId = UUID.randomUUID();
        PolicyIssuedEvent event = new PolicyIssuedEvent(policyId, quotationId, "");

        // When
        Mono<Void> result = publisher.publishPolicyIssued(event);

        // Then
        StepVerifier.create(result)
                .verifyComplete();
    }

    @Test
    @DisplayName("should return Mono<Void> type")
    void shouldReturnMonoVoid() {
        // Given
        PolicyIssuedEvent event = new PolicyIssuedEvent(
                UUID.randomUUID(),
                UUID.randomUUID(),
                "test@example.com");

        // When
        Mono<Void> result = publisher.publishPolicyIssued(event);

        // Then
        assertNotNull(result);
        assertInstanceOf(Mono.class, result);
    }

    @Test
    @DisplayName("should complete synchronously")
    void shouldCompleteSynchronously() {
        // Given
        PolicyIssuedEvent event = new PolicyIssuedEvent(
                UUID.randomUUID(),
                UUID.randomUUID(),
                "test@example.com");

        // When & Then
        assertDoesNotThrow(() -> {
            publisher.publishPolicyIssued(event).block();
        });
    }
}
