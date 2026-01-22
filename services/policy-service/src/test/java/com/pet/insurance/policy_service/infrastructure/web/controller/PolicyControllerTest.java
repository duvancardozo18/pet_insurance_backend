package com.pet.insurance.policy_service.infrastructure.web.controller;

import com.pet.insurance.policy_service.application.usecase.IssuePolicyUseCase;
import com.pet.insurance.policy_service.domain.model.Owner;
import com.pet.insurance.policy_service.domain.model.Policy;
import com.pet.insurance.policy_service.infrastructure.web.request.IssuePolicyRequest;
import com.pet.insurance.policy_service.infrastructure.web.response.IssuePolicyResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("PolicyController Tests")
class PolicyControllerTest {

    @Mock
    private IssuePolicyUseCase issuePolicyUseCase;

    @InjectMocks
    private PolicyController policyController;

    private UUID quotationId;
    private Owner testOwner;
    private Policy testPolicy;
    private IssuePolicyRequest testRequest;

    @BeforeEach
    void setUp() {
        quotationId = UUID.randomUUID();
        testOwner = new Owner("owner-123", "John Doe", "john.doe@email.com");
        testPolicy = Policy.issue(quotationId, testOwner);

        testRequest = new IssuePolicyRequest(
                quotationId.toString(),
                "owner-123",
                "John Doe",
                "john.doe@email.com");
    }

    @Test
    @DisplayName("should issue policy successfully")
    void shouldIssuePolicySuccessfully() {
        // Arrange
        when(issuePolicyUseCase.execute(
                anyString(),
                anyString(),
                anyString(),
                anyString())).thenReturn(Mono.just(testPolicy));

        // Act & Assert
        StepVerifier.create(policyController.issuePolicy(testRequest))
                .assertNext(response -> {
                    assertNotNull(response);
                    assertEquals(testPolicy.getId().toString(), response.policyId());
                    assertEquals(quotationId.toString(), response.quotationId());
                    assertTrue(response.active());
                })
                .verifyComplete();

        verify(issuePolicyUseCase, times(1)).execute(
                testRequest.quotationId(),
                testRequest.ownerId(),
                testRequest.ownerName(),
                testRequest.ownerEmail());
    }

    @Test
    @DisplayName("should call use case with correct parameters")
    void shouldCallUseCaseWithCorrectParameters() {
        // Arrange
        when(issuePolicyUseCase.execute(
                anyString(),
                anyString(),
                anyString(),
                anyString())).thenReturn(Mono.just(testPolicy));

        // Act
        policyController.issuePolicy(testRequest).block();

        // Assert
        verify(issuePolicyUseCase, times(1)).execute(
                quotationId.toString(),
                "owner-123",
                "John Doe",
                "john.doe@email.com");
    }

    @Test
    @DisplayName("should return response with correct policy data")
    void shouldReturnResponseWithCorrectPolicyData() {
        // Arrange
        when(issuePolicyUseCase.execute(
                anyString(),
                anyString(),
                anyString(),
                anyString())).thenReturn(Mono.just(testPolicy));

        // Act
        Mono<IssuePolicyResponse> responseMono = policyController.issuePolicy(testRequest);

        // Assert
        StepVerifier.create(responseMono)
                .assertNext(response -> {
                    assertNotNull(response.policyId());
                    assertEquals(quotationId.toString(), response.quotationId());
                    assertTrue(response.active());
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("should propagate error from use case")
    void shouldPropagateErrorFromUseCase() {
        // Arrange
        RuntimeException expectedException = new RuntimeException("Use case error");
        when(issuePolicyUseCase.execute(
                anyString(),
                anyString(),
                anyString(),
                anyString())).thenReturn(Mono.error(expectedException));

        // Act & Assert
        StepVerifier.create(policyController.issuePolicy(testRequest))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                        throwable.getMessage().equals("Use case error"))
                .verify();

        verify(issuePolicyUseCase, times(1)).execute(
                anyString(),
                anyString(),
                anyString(),
                anyString());
    }

    @Test
    @DisplayName("should handle multiple policy issuance requests")
    void shouldHandleMultiplePolicyIssuanceRequests() {
        // Arrange
        IssuePolicyRequest request1 = new IssuePolicyRequest(
                UUID.randomUUID().toString(),
                "owner-1",
                "Owner One",
                "owner1@email.com");
        IssuePolicyRequest request2 = new IssuePolicyRequest(
                UUID.randomUUID().toString(),
                "owner-2",
                "Owner Two",
                "owner2@email.com");

        Owner owner1 = new Owner("owner-1", "Owner One", "owner1@email.com");
        Owner owner2 = new Owner("owner-2", "Owner Two", "owner2@email.com");
        Policy policy1 = Policy.issue(UUID.fromString(request1.quotationId()), owner1);
        Policy policy2 = Policy.issue(UUID.fromString(request2.quotationId()), owner2);

        when(issuePolicyUseCase.execute(
                eq(request1.quotationId()),
                anyString(),
                anyString(),
                anyString())).thenReturn(Mono.just(policy1));

        when(issuePolicyUseCase.execute(
                eq(request2.quotationId()),
                anyString(),
                anyString(),
                anyString())).thenReturn(Mono.just(policy2));

        // Act & Assert
        StepVerifier.create(policyController.issuePolicy(request1))
                .assertNext(response -> {
                    assertEquals(request1.quotationId(), response.quotationId());
                    assertTrue(response.active());
                })
                .verifyComplete();

        StepVerifier.create(policyController.issuePolicy(request2))
                .assertNext(response -> {
                    assertEquals(request2.quotationId(), response.quotationId());
                    assertTrue(response.active());
                })
                .verifyComplete();

        verify(issuePolicyUseCase, times(2)).execute(
                anyString(),
                anyString(),
                anyString(),
                anyString());
    }

    @Test
    @DisplayName("should return mono response type")
    void shouldReturnMonoResponseType() {
        // Arrange
        when(issuePolicyUseCase.execute(
                anyString(),
                anyString(),
                anyString(),
                anyString())).thenReturn(Mono.just(testPolicy));

        // Act
        Mono<IssuePolicyResponse> result = policyController.issuePolicy(testRequest);

        // Assert
        assertNotNull(result);
        assertInstanceOf(Mono.class, result);
    }
}
