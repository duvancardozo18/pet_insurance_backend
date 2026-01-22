package com.pet.insurance.policy_service.application.usecase;

import com.pet.insurance.policy_service.domain.exception.QuotationExpiredException;
import com.pet.insurance.policy_service.domain.model.Policy;
import com.pet.insurance.policy_service.domain.model.Quotation;
import com.pet.insurance.policy_service.domain.port.PolicyRepository;
import com.pet.insurance.policy_service.domain.port.QuotationClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IssuePolicyUseCaseTest {

    @Mock
    private PolicyRepository policyRepository;

    @Mock
    private QuotationClient quotationClient;

    private IssuePolicyUseCase issuePolicyUseCase;

    @BeforeEach
    void setUp() {
        issuePolicyUseCase = new IssuePolicyUseCase(policyRepository, quotationClient);
    }

    @Test
    void shouldIssuePolicySuccessfully() {
        // Given
        String quotationId = UUID.randomUUID().toString();
        String ownerId = "owner123";
        String ownerName = "John Doe";
        String ownerEmail = "john.doe@example.com";

        Quotation quotation = createValidQuotation(quotationId);
        Policy expectedPolicy = mock(Policy.class);

        when(quotationClient.findById(quotationId)).thenReturn(Mono.just(quotation));
        when(policyRepository.save(any(Policy.class))).thenReturn(Mono.just(expectedPolicy));

        // When
        Mono<Policy> result = issuePolicyUseCase.execute(quotationId, ownerId, ownerName, ownerEmail);

        // Then
        StepVerifier.create(result)
                .expectNext(expectedPolicy)
                .verifyComplete();

        verify(quotationClient, times(1)).findById(quotationId);
        verify(policyRepository, times(1)).save(any(Policy.class));
    }

    @Test
    void shouldReturnErrorWhenQuotationIsExpired() {
        // Given
        String quotationId = UUID.randomUUID().toString();
        String ownerId = "owner123";
        String ownerName = "John Doe";
        String ownerEmail = "john.doe@example.com";

        Quotation expiredQuotation = createExpiredQuotation(quotationId);

        when(quotationClient.findById(quotationId)).thenReturn(Mono.just(expiredQuotation));

        // When
        Mono<Policy> result = issuePolicyUseCase.execute(quotationId, ownerId, ownerName, ownerEmail);

        // Then
        StepVerifier.create(result)
                .expectErrorMatches(error -> error instanceof QuotationExpiredException &&
                        error.getMessage().contains(quotationId))
                .verify();

        verify(quotationClient, times(1)).findById(quotationId);
        verify(policyRepository, never()).save(any(Policy.class));
    }

    @Test
    void shouldReturnErrorWhenQuotationNotFound() {
        // Given
        String quotationId = UUID.randomUUID().toString();
        String ownerId = "owner123";
        String ownerName = "John Doe";
        String ownerEmail = "john.doe@example.com";

        when(quotationClient.findById(quotationId)).thenReturn(Mono.empty());

        // When
        Mono<Policy> result = issuePolicyUseCase.execute(quotationId, ownerId, ownerName, ownerEmail);

        // Then
        StepVerifier.create(result)
                .verifyComplete();

        verify(quotationClient, times(1)).findById(quotationId);
        verify(policyRepository, never()).save(any(Policy.class));
    }

    @Test
    void shouldReturnErrorWhenRepositoryFails() {
        // Given
        String quotationId = UUID.randomUUID().toString();
        String ownerId = "owner123";
        String ownerName = "John Doe";
        String ownerEmail = "john.doe@example.com";

        Quotation quotation = createValidQuotation(quotationId);
        RuntimeException exception = new RuntimeException("Database error");

        when(quotationClient.findById(quotationId)).thenReturn(Mono.just(quotation));
        when(policyRepository.save(any(Policy.class))).thenReturn(Mono.error(exception));

        // When
        Mono<Policy> result = issuePolicyUseCase.execute(quotationId, ownerId, ownerName, ownerEmail);

        // Then
        StepVerifier.create(result)
                .expectErrorMatches(error -> error instanceof RuntimeException &&
                        error.getMessage().equals("Database error"))
                .verify();

        verify(quotationClient, times(1)).findById(quotationId);
        verify(policyRepository, times(1)).save(any(Policy.class));
    }

    @Test
    void shouldVerifyOwnerDataIsCorrectlyPassed() {
        // Given
        String quotationId = UUID.randomUUID().toString();
        String ownerId = "owner456";
        String ownerName = "Jane Smith";
        String ownerEmail = "jane.smith@example.com";

        Quotation quotation = createValidQuotation(quotationId);

        when(quotationClient.findById(quotationId)).thenReturn(Mono.just(quotation));
        when(policyRepository.save(any(Policy.class))).thenAnswer(invocation -> {
            Policy policy = invocation.getArgument(0);
            assertEquals(ownerId, policy.getOwner().id());
            assertEquals(ownerName, policy.getOwner().name());
            assertEquals(ownerEmail, policy.getOwner().email());
            return Mono.just(policy);
        });

        // When
        Mono<Policy> result = issuePolicyUseCase.execute(quotationId, ownerId, ownerName, ownerEmail);

        // Then
        StepVerifier.create(result)
                .expectNextMatches(policy -> policy.getOwner().id().equals(ownerId) &&
                        policy.getOwner().name().equals(ownerName) &&
                        policy.getOwner().email().equals(ownerEmail))
                .verifyComplete();
    }

    private Quotation createValidQuotation(String quotationId) {
        return Quotation.reconstruct(
                quotationId,
                "Max",
                "Dog",
                "Labrador",
                3,
                false,
                BigDecimal.valueOf(50.00),
                LocalDate.now().plusDays(7));
    }

    private Quotation createExpiredQuotation(String quotationId) {
        return Quotation.reconstruct(
                quotationId,
                "Max",
                "Dog",
                "Labrador",
                3,
                false,
                BigDecimal.valueOf(50.00),
                LocalDate.now().minusDays(1));
    }
}
