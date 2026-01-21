package com.pet.insurance.policy_service.infrastructure.driven.persistence.mongo;

import com.pet.insurance.policy_service.domain.model.Owner;
import com.pet.insurance.policy_service.domain.model.Policy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("MongoPolicyRepositoryAdapter Tests")
class MongoPolicyRepositoryAdapterTest {

    @Mock
    private SpringDataPolicyRepository springDataRepository;

    @InjectMocks
    private MongoPolicyRepositoryAdapter repositoryAdapter;

    private Policy testPolicy;
    private PolicyDocument testDocument;
    private Owner testOwner;
    private UUID quotationId;

    @BeforeEach
    void setUp() {
        quotationId = UUID.randomUUID();
        testOwner = new Owner("owner-123", "John Doe", "john.doe@email.com");
        testPolicy = Policy.issue(quotationId, testOwner);

        testDocument = new PolicyDocument();
        testDocument.setId(testPolicy.getId().toString());
        testDocument.setQuotationId(quotationId);
        testDocument.setOwnerId("owner-123");
        testDocument.setOwnerName("John Doe");
        testDocument.setOwnerEmail("john.doe@email.com");
        testDocument.setStartDate(LocalDate.now());
        testDocument.setEndDate(LocalDate.now().plusYears(1));
        testDocument.setActive(true);
    }

    @Test
    @DisplayName("should save policy successfully")
    void shouldSavePolicySuccessfully() {
        // Arrange
        when(springDataRepository.save(any(PolicyDocument.class)))
                .thenReturn(Mono.just(testDocument));

        // Act & Assert
        StepVerifier.create(repositoryAdapter.save(testPolicy))
                .assertNext(savedPolicy -> {
                    assertNotNull(savedPolicy);
                    assertNotNull(savedPolicy.getId());
                    assertEquals(quotationId, savedPolicy.getQuotationId());
                    assertEquals("owner-123", savedPolicy.getOwner().id());
                    assertEquals("John Doe", savedPolicy.getOwner().name());
                    assertEquals("john.doe@email.com", savedPolicy.getOwner().email());
                    assertTrue(savedPolicy.isActive());
                })
                .verifyComplete();

        // Verify
        verify(springDataRepository, times(1)).save(any(PolicyDocument.class));
    }

    @Test
    @DisplayName("should convert policy to document correctly")
    void shouldConvertPolicyToDocumentCorrectly() {
        // Arrange
        ArgumentCaptor<PolicyDocument> documentCaptor = ArgumentCaptor.forClass(PolicyDocument.class);
        when(springDataRepository.save(any(PolicyDocument.class)))
                .thenReturn(Mono.just(testDocument));

        // Act
        repositoryAdapter.save(testPolicy).block();

        // Assert
        verify(springDataRepository).save(documentCaptor.capture());
        PolicyDocument capturedDocument = documentCaptor.getValue();

        assertNotNull(capturedDocument);
        assertEquals(testPolicy.getId().toString(), capturedDocument.getId());
        assertEquals(testPolicy.getQuotationId(), capturedDocument.getQuotationId());
        assertEquals(testPolicy.getOwner().id(), capturedDocument.getOwnerId());
        assertEquals(testPolicy.getOwner().name(), capturedDocument.getOwnerName());
        assertEquals(testPolicy.getOwner().email(), capturedDocument.getOwnerEmail());
        assertEquals(testPolicy.getStartDate(), capturedDocument.getStartDate());
        assertEquals(testPolicy.getEndDate(), capturedDocument.getEndDate());
        assertEquals(testPolicy.isActive(), capturedDocument.isActive());
    }

    @Test
    @DisplayName("should find policy by id successfully")
    void shouldFindPolicyByIdSuccessfully() {
        // Arrange
        UUID policyId = UUID.fromString(testDocument.getId());
        when(springDataRepository.findById(testDocument.getId()))
                .thenReturn(Mono.just(testDocument));

        // Act & Assert
        StepVerifier.create(repositoryAdapter.findById(policyId))
                .assertNext(foundPolicy -> {
                    assertNotNull(foundPolicy);
                    assertEquals(quotationId, foundPolicy.getQuotationId());
                    assertEquals("owner-123", foundPolicy.getOwner().id());
                    assertEquals("John Doe", foundPolicy.getOwner().name());
                    assertEquals("john.doe@email.com", foundPolicy.getOwner().email());
                })
                .verifyComplete();

        verify(springDataRepository, times(1)).findById(testDocument.getId());
    }

    @Test
    @DisplayName("should return empty when policy not found by id")
    void shouldReturnEmptyWhenPolicyNotFoundById() {
        // Arrange
        UUID policyId = UUID.randomUUID();
        when(springDataRepository.findById(policyId.toString()))
                .thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(repositoryAdapter.findById(policyId))
                .verifyComplete();

        verify(springDataRepository, times(1)).findById(policyId.toString());
    }

    @Test
    @DisplayName("should handle save error gracefully")
    void shouldHandleSaveErrorGracefully() {
        // Arrange
        when(springDataRepository.save(any(PolicyDocument.class)))
                .thenReturn(Mono.error(new RuntimeException("Database error")));

        // Act & Assert
        StepVerifier.create(repositoryAdapter.save(testPolicy))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                        throwable.getMessage().equals("Database error"))
                .verify();

        verify(springDataRepository, times(1)).save(any(PolicyDocument.class));
    }

    @Test
    @DisplayName("should handle find by id error gracefully")
    void shouldHandleFindByIdErrorGracefully() {
        // Arrange
        UUID policyId = UUID.randomUUID();
        when(springDataRepository.findById(policyId.toString()))
                .thenReturn(Mono.error(new RuntimeException("Database error")));

        // Act & Assert
        StepVerifier.create(repositoryAdapter.findById(policyId))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                        throwable.getMessage().equals("Database error"))
                .verify();

        verify(springDataRepository, times(1)).findById(policyId.toString());
    }
}
