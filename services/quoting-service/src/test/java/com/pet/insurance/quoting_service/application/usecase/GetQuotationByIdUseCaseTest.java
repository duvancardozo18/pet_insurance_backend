package com.pet.insurance.quoting_service.application.usecase;

import com.pet.insurance.quoting_service.domain.model.Quotation;
import com.pet.insurance.quoting_service.domain.port.QuotationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetQuotationByIdUseCaseTest {

    @Mock
    private QuotationRepository repository;

    private GetQuotationByIdUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new GetQuotationByIdUseCase(repository);
    }

    @Test
    void shouldReturnQuotationWhenFound() {
        // Given
        String quotationId = "test-id-123";
        Quotation expectedQuotation = Quotation.create(
                "Max",
                "DOG",
                "Golden Retriever",
                3,
                false,
                BigDecimal.TEN);

        when(repository.findById(quotationId)).thenReturn(Mono.just(expectedQuotation));

        // When
        Mono<Quotation> result = useCase.execute(quotationId);

        // Then
        StepVerifier.create(result)
                .expectNext(expectedQuotation)
                .verifyComplete();

        verify(repository, times(1)).findById(quotationId);
    }

    @Test
    void shouldReturnEmptyMonoWhenQuotationNotFound() {
        // Given
        String quotationId = "non-existent-id";
        when(repository.findById(quotationId)).thenReturn(Mono.empty());

        // When
        Mono<Quotation> result = useCase.execute(quotationId);

        // Then
        StepVerifier.create(result)
                .verifyComplete();

        verify(repository, times(1)).findById(quotationId);
    }

    @Test
    void shouldReturnQuotationWithPremiumPlan() {
        // Given
        String quotationId = "premium-id";
        Quotation premiumQuotation = Quotation.create(
                "Luna",
                "CAT",
                "Persian",
                2,
                true,
                new BigDecimal("20.00"));

        when(repository.findById(quotationId)).thenReturn(Mono.just(premiumQuotation));

        // When
        Mono<Quotation> result = useCase.execute(quotationId);

        // Then
        StepVerifier.create(result)
                .assertNext(quotation -> {
                    assertEquals("Luna", quotation.petName());
                    assertTrue(quotation.premiumPlan());
                    assertEquals(new BigDecimal("20.00"), quotation.price());
                })
                .verifyComplete();

        verify(repository, times(1)).findById(quotationId);
    }

    @Test
    void shouldReturnExpiredQuotation() {
        // Given
        String quotationId = "expired-id";
        Quotation expiredQuotation = Quotation.reconstruct(
                quotationId,
                "Old Max",
                "DOG",
                "Beagle",
                8,
                false,
                BigDecimal.TEN,
                LocalDate.now().minusDays(1));

        when(repository.findById(quotationId)).thenReturn(Mono.just(expiredQuotation));

        // When
        Mono<Quotation> result = useCase.execute(quotationId);

        // Then
        StepVerifier.create(result)
                .assertNext(quotation -> {
                    assertEquals(quotationId, quotation.id());
                    assertTrue(quotation.isExpired());
                })
                .verifyComplete();

        verify(repository, times(1)).findById(quotationId);
    }

    @Test
    void shouldPropagateRepositoryError() {
        // Given
        String quotationId = "error-id";
        RuntimeException error = new RuntimeException("Database connection error");
        when(repository.findById(quotationId)).thenReturn(Mono.error(error));

        // When
        Mono<Quotation> result = useCase.execute(quotationId);

        // Then
        StepVerifier.create(result)
                .expectError(RuntimeException.class)
                .verify();

        verify(repository, times(1)).findById(quotationId);
    }

    @Test
    void shouldHandleDifferentIdFormats() {
        // Given
        String[] ids = { "123", "abc-def-123", "uuid-format-id", "simple" };

        for (String id : ids) {
            Quotation quotation = Quotation.create("Pet", "DOG", "Mix", 3, false, BigDecimal.TEN);
            when(repository.findById(id)).thenReturn(Mono.just(quotation));

            // When
            Mono<Quotation> result = useCase.execute(id);

            // Then
            StepVerifier.create(result)
                    .expectNext(quotation)
                    .verifyComplete();
        }

        verify(repository, times(ids.length)).findById(anyString());
    }

    @Test
    void shouldReturnQuotationWithMaximumAge() {
        // Given
        String quotationId = "old-pet-id";
        Quotation oldPetQuotation = Quotation.create(
                "Very Old Dog",
                "DOG",
                "Mix",
                10,
                false,
                new BigDecimal("25.00"));

        when(repository.findById(quotationId)).thenReturn(Mono.just(oldPetQuotation));

        // When
        Mono<Quotation> result = useCase.execute(quotationId);

        // Then
        StepVerifier.create(result)
                .assertNext(quotation -> {
                    assertEquals(10, quotation.age());
                    assertEquals("Very Old Dog", quotation.petName());
                })
                .verifyComplete();

        verify(repository, times(1)).findById(quotationId);
    }

    @Test
    void shouldReturnQuotationWithMinimumAge() {
        // Given
        String quotationId = "puppy-id";
        Quotation puppyQuotation = Quotation.create(
                "Puppy",
                "DOG",
                "Chihuahua",
                0,
                false,
                BigDecimal.TEN);

        when(repository.findById(quotationId)).thenReturn(Mono.just(puppyQuotation));

        // When
        Mono<Quotation> result = useCase.execute(quotationId);

        // Then
        StepVerifier.create(result)
                .assertNext(quotation -> {
                    assertEquals(0, quotation.age());
                    assertEquals("Puppy", quotation.petName());
                })
                .verifyComplete();

        verify(repository, times(1)).findById(quotationId);
    }
}
