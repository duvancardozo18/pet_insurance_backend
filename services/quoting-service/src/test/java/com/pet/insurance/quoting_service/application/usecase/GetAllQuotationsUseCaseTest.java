package com.pet.insurance.quoting_service.application.usecase;

import com.pet.insurance.quoting_service.domain.model.Quotation;
import com.pet.insurance.quoting_service.domain.port.QuotationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetAllQuotationsUseCaseTest {

    @Mock
    private QuotationRepository repository;

    private GetAllQuotationsUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new GetAllQuotationsUseCase(repository);
    }

    @Test
    void shouldReturnAllQuotations() {
        // Given
        Quotation quotation1 = Quotation.create("Max", "DOG", "Golden Retriever", 3, false, BigDecimal.TEN);
        Quotation quotation2 = Quotation.create("Luna", "CAT", "Persian", 2, true, new BigDecimal("20.00"));
        Quotation quotation3 = Quotation.create("Rocky", "DOG", "Bulldog", 5, false, new BigDecimal("15.00"));

        when(repository.findAll()).thenReturn(Flux.just(quotation1, quotation2, quotation3));

        // When
        Flux<Quotation> result = useCase.execute();

        // Then
        StepVerifier.create(result)
                .expectNext(quotation1)
                .expectNext(quotation2)
                .expectNext(quotation3)
                .verifyComplete();

        verify(repository, times(1)).findAll();
    }

    @Test
    void shouldReturnEmptyFluxWhenNoQuotations() {
        // Given
        when(repository.findAll()).thenReturn(Flux.empty());

        // When
        Flux<Quotation> result = useCase.execute();

        // Then
        StepVerifier.create(result)
                .verifyComplete();

        verify(repository, times(1)).findAll();
    }

    @Test
    void shouldReturnSingleQuotation() {
        // Given
        Quotation quotation = Quotation.create("Max", "DOG", "Golden Retriever", 3, false, BigDecimal.TEN);
        when(repository.findAll()).thenReturn(Flux.just(quotation));

        // When
        Flux<Quotation> result = useCase.execute();

        // Then
        StepVerifier.create(result)
                .expectNext(quotation)
                .verifyComplete();

        verify(repository, times(1)).findAll();
    }

    @Test
    void shouldPropagateRepositoryError() {
        // Given
        RuntimeException error = new RuntimeException("Database connection error");
        when(repository.findAll()).thenReturn(Flux.error(error));

        // When
        Flux<Quotation> result = useCase.execute();

        // Then
        StepVerifier.create(result)
                .expectError(RuntimeException.class)
                .verify();

        verify(repository, times(1)).findAll();
    }

    @Test
    void shouldReturnQuotationsIncludingExpiredOnes() {
        // Given
        Quotation activeQuotation = Quotation.create("Max", "DOG", "Mix", 3, false, BigDecimal.TEN);
        Quotation expiredQuotation = Quotation.reconstruct(
                "expired-id",
                "Old Quote",
                "CAT",
                "Persian",
                5,
                false,
                BigDecimal.TEN,
                LocalDate.now().minusDays(1));

        when(repository.findAll()).thenReturn(Flux.just(activeQuotation, expiredQuotation));

        // When
        Flux<Quotation> result = useCase.execute();

        // Then
        StepVerifier.create(result)
                .expectNext(activeQuotation)
                .expectNext(expiredQuotation)
                .verifyComplete();

        verify(repository, times(1)).findAll();
    }

    @Test
    void shouldReturnQuotationsWithDifferentPlanTypes() {
        // Given
        Quotation basicPlan = Quotation.create("Max", "DOG", "Mix", 3, false, BigDecimal.TEN);
        Quotation premiumPlan = Quotation.create("Luna", "CAT", "Persian", 2, true, new BigDecimal("20.00"));

        when(repository.findAll()).thenReturn(Flux.just(basicPlan, premiumPlan));

        // When
        Flux<Quotation> result = useCase.execute();

        // Then
        StepVerifier.create(result)
                .expectNextMatches(q -> !q.premiumPlan())
                .expectNextMatches(q -> q.premiumPlan())
                .verifyComplete();

        verify(repository, times(1)).findAll();
    }
}
